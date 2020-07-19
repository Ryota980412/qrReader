package com.example0.sakai.qrreader

import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.AndroidRuntimeException
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class QrCodeDisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_display)
        val data = "http://osa030.hatenablog.com/"
        val size = 500
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap =
                barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, size, size)
            val imageView =
                findViewById<View>(R.id.imageView) as ImageView
            imageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            throw AndroidRuntimeException("Barcode Error.", e)
        }
    }
}