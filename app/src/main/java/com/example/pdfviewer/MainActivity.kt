package com.example.pdfviewer


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var pdfView: PDFView
    var pdfUrl = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf"
    private lateinit var pdfViewModel: PDFViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pdfView = findViewById(R.id.idPDFView)
        pdfViewModel = ViewModelProvider(this).get(PDFViewModel::class.java)

        loadPDFFromURL(pdfUrl)
    }

    private fun loadPDFFromURL(pdfUrl: String) {
        lifecycleScope.launch {
            try {
                val inputStream = withContext(Dispatchers.IO) {
                    fetchPDFStream(pdfUrl)
                }
                pdfView.fromStream(inputStream).load()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchPDFStream(pdfUrl: String): InputStream? {
        return try {
            val url = URL(pdfUrl)
            val urlConnection = url.openConnection() as HttpURLConnection

            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                BufferedInputStream(urlConnection.inputStream)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

class PDFViewModel : ViewModel() {
    // ViewModel class (if needed) for handling data and state
}
