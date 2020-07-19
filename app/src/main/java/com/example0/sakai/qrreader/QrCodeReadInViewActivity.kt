package com.example0.sakai.qrreader

//import android.support.v7.app.AppCompatActivity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*

//import java.util.logging.Handler


open class QrCodeReadInViewActivity : AppCompatActivity() {
    private var mBarcodeView: CompoundBarcodeView? = null
    private val qrList = ArrayList<String>()

    private var counter:Int = 0
    lateinit var textView:TextView
    private val handler = Handler()

    val mydirName = "test"
    val ExtFileName = "testfile.csv"
    // 現在の外部ストレージのログ・ファイル名(パス含め)
    private val extFilePath: String
        get() {
            val myDir = Environment.getExternalStorageDirectory().getPath() +"/"+mydirName
            return  myDir+"/"+ ExtFileName
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //var textView:TextView
        setContentView(R.layout.activity_qr_code_read_in_view)
        textView = findViewById<View>(R.id.textView) as TextView
        mBarcodeView = findViewById<View>(R.id.barcodeView) as CompoundBarcodeView
        //val runnable = object : Runnable{
            //override fun run(){
                //mBarcodeView = findViewById<View>(R.id.barcodeView) as CompoundBarcodeView
                mBarcodeView!!.decodeContinuous(object : BarcodeCallback {
                    //mBarcodeView!!.decodeSingle(object : BarcodeCallback {
                    override fun barcodeResult(barcodeResult: BarcodeResult) {
                        //textView = findViewById<View>(R.id.textView) as TextView
                        if(textView.text!=barcodeResult.text) {
                            textView.text = barcodeResult.text
                            qrList.add(barcodeResult.text.toString())
                        }
                    }

                    override fun possibleResultPoints(list: List<ResultPoint>) {}

                })
                //handler.postDelayed(this, 5000)
          //  }
        //}
        /*if(counter==1){
            handler.removeCallbacks(runnable)
        }
        if(counter==2){
          //  handler.post(runnable)
            counter=0
        }*/


    }

    public  override fun onStart() {
        super.onStart()

        //外部許可のリクエスト
        // リクエスト識別用のユニークな値
        val REQUEST_PERMISSIONS_ID = 1000
        // リクエスト用
        val reqPermissions = ArrayList<String>()
        // リクエスト に追加
        reqPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        // パーミション確認
        ActivityCompat.requestPermissions(this, reqPermissions.toTypedArray(), REQUEST_PERMISSIONS_ID)

        //外部許可の結果確認
        // 承認の結果(オペレータの操作)をこのコールバックで判断
        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            if (requestCode == REQUEST_PERMISSIONS_ID) {
                // 識別IDでリクエストを判断
                if (grantResults.isNotEmpty()) {
                    // 処理された
                    for (i in permissions.indices) {
                        // 複数リクエストがあった場合
                        if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                            // 外部ストレージのパーミッション
                            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                                // 許可
                            } else {
                                // 拒否
                            }
                        }
                    }
                }
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        mBarcodeView!!.resume()
        /*while(counter==0){
            mBarcodeView!!.decodeSingle(object : BarcodeCallback {
                override fun barcodeResult(barcodeResult: BarcodeResult) {
                    //textView = findViewById<View>(R.id.textView) as TextView
                    textView.text = barcodeResult.text
                    qrList.add(barcodeResult.text.toString())
                }

                override fun possibleResultPoints(list: List<ResultPoint>) {}

            })

        }*/
    }

    public override fun onPause() {
        super.onPause()
        mBarcodeView!!.pause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item1 -> {
                //qrList.clear()
                counter=1
                mBarcodeView!!.pause()

                if(!qrList.isEmpty()){
                    saveFile(ExtFileName, qrList)
                } else {
                    textView.text=getString(R.string.Empty)
                }

                /*val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val testfile = "testfile.txt"
                val file = File(path, testfile)
                try {
                    FileOutputStream(file, true).use({ fileOutputStream ->
                        OutputStreamWriter(
                            fileOutputStream,
                            StandardCharsets.UTF_8
                        ).use({ outputStreamWriter ->
                            BufferedWriter(outputStreamWriter).use({ bw ->
                                bw.write(str)
                                bw.flush()
                            })
                        })
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }*/

                return true
            }
            R.id.item2 -> {
                textView.text= getString(R.string.hello_world)
                qrList.clear()
                counter=2
                mBarcodeView!!.resume()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveFile(file: String, list: List<String>){
        val myDir = File(Environment.getExternalStorageDirectory(), mydirName)
        if(!myDir.exists()){
            myDir.mkdirs()
        }
        val file = File(extFilePath)
        try {
            FileOutputStream(file, true).use { fileOutputStream ->
                OutputStreamWriter(fileOutputStream, "UTF-8").use { outputStreamWriter ->
                    BufferedWriter(outputStreamWriter).use { bw ->
                        for(s in list){
                            bw.write(s+"\n")
                        }
                        bw.flush()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /*File(applicationContext.filesDir, file).writer().use{
            for(s in list){
                it.write(s)
            }
        }*/
    }

}