package com.raystatic.iconfinder.ui.viewmodels

import android.content.Context
import android.content.ContextWrapper
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.raystatic.iconfinder.data.IconsRepository
import com.raystatic.iconfinder.data.models.Icon
import com.raystatic.iconfinder.utils.Constants
import com.raystatic.iconfinder.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.*
import java.lang.Exception

class SearchViewModel @ViewModelInject constructor(
    private val repository: IconsRepository
):ViewModel() {

    private val _selectedIcon = MutableLiveData<Icon?>()

    val selectedIcon:LiveData<Icon?> get() = _selectedIcon

    fun setSelectedIcon(icon: Icon){
        _selectedIcon.postValue(icon)
    }

    private val currentIconQuery = MutableLiveData(DEFAULT_QUERY)

    val icons = currentIconQuery.switchMap { currentIconQuery->
        repository.getSearchResult(query = currentIconQuery).cachedIn(viewModelScope)
    }

    fun searchIcons(query:String){
        currentIconQuery.value = query
    }

    private val _downloadResponse = MutableLiveData<Resource<ResponseBody>>()

    val downloadResponse:LiveData<Resource<ResponseBody>> get() = _downloadResponse

    fun downloadIcon(url:String) = viewModelScope.launch {
        _downloadResponse.postValue(Resource.loading(null))
        try {
            repository.downloadIcon(url).also {
                if (it.isSuccessful){
                    _downloadResponse.postValue(Resource.success(it.body()))
                }else{
                    _downloadResponse.postValue(Resource.error(Constants.DOWNLOAD_FAILED, null))
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            _downloadResponse.postValue(Resource.error(Constants.SOMETHING_WENT_WRONG, null))
        }
    }

    private val _savedFile = MutableLiveData<Resource<String>>()

    val savedFile:LiveData<Resource<String>> get() = _savedFile

    fun writeFileToDevice(responseBody: ResponseBody, context:Context) = viewModelScope.launch {
        writeFileToDeviceInBackground(responseBody, context)
    }

    private suspend fun writeFileToDeviceInBackground(responseBody: ResponseBody?, context: Context) = withContext(Dispatchers.IO){

        _savedFile.postValue(Resource.loading(null))

        var inputStream:InputStream?=null
        var outputStream:OutputStream?=null

        try {
            try {
                val contextWrapper = ContextWrapper(context)
                val iconDirectory = contextWrapper.getDir(Constants.ICONS_DIRECTORY,Context.MODE_PRIVATE)
                val iconFile = File(iconDirectory,"${System.currentTimeMillis()}.png")

                val filReader = ByteArray(size = 4096)

                val fileSize = responseBody?.contentLength()
                var fileSizeDownloaded = 0

                inputStream = responseBody?.byteStream()
                outputStream = FileOutputStream(iconFile)

                while (true){
                    var read = inputStream?.read(filReader)
                    if (read == -1) break
                    outputStream.write(filReader,0, read!!)
                    fileSizeDownloaded+=read

                    Timber.d("File downloaded: $fileSizeDownloaded of $fileSize")
                }

                outputStream.flush()

                _savedFile.postValue(Resource.success(iconFile.absolutePath))

                return@withContext true

            }catch (e:IOException){
                e.printStackTrace()
                _savedFile.postValue(Resource.error(Constants.ERROR_SAVING_FILE, null))
                return@withContext false
            }catch (e:FileNotFoundException){
                e.printStackTrace()
                _savedFile.postValue(Resource.error(Constants.ERROR_SAVING_FILE, null))
                return@withContext  false
            }
            finally {
                inputStream?.close()

                outputStream?.close()
            }
        }catch (e:IOException){
            e.printStackTrace()
            _savedFile.postValue(Resource.error(Constants.ERROR_SAVING_FILE, null))
            return@withContext false
        }
    }

    companion object{
        private const val DEFAULT_QUERY = "arrow"
    }

}