package com.example.app_part2_chap07

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private val soundVisualizerView: SoundVisualizerView by lazy{
        findViewById(R.id.soundVisualizerView)
    }
    private val recordTimeTextView: CountUpView by lazy{
        findViewById(R.id.recordTimeTextView)
    }

    private val resetButton: Button by lazy {
        findViewById(R.id.resetButton)
    }
    private val recordButton: RecordButton by lazy {
        findViewById(R.id.recordButton)
    }

    private val requiredPermission = arrayOf(android.Manifest.permission.RECORD_AUDIO,android.Manifest.permission.READ_EXTERNAL_STORAGE)
    private val recordingFilePath: String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }
    private var recorder: MediaRecorder?=null
    private var player: MediaPlayer?=null
    private var state = State.BEFORE_RECORDING
        set(value){
            field = value
            resetButton.isEnabled = (value == State.AFTER_RECORDING) ||
                    (value == State.ON_PLAYING)
            recordButton.updateIconwithState(value)
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission()
        initView()
        bindViews()
        initVariables()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted = requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

       if(!audioRecordPermissionGranted){
           finish()
       }
    }

    private fun requestAudioPermission(){
        requestPermissions(requiredPermission, REQUEST_RECORD_AUDIO_PERMISSION)
    }
    private fun initView(){

        recordButton.updateIconwithState(state)
    }

    private fun bindViews(){
        soundVisualizerView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude ?: 0
        }
        resetButton.setOnClickListener {
            stopPlaying()
            soundVisualizerView.clearVisualization()
            recordTimeTextView.clearCountTime()
            state = State.BEFORE_RECORDING
        }
        recordButton.setOnClickListener{
            when(state){
                State.BEFORE_RECORDING->{
                    startRecordiong()
                }
                State.ON_RECORDING->{
                    stopRecording()
                }
                State.AFTER_RECORDING->{
                    startPlaying()
                }
                State.ON_PLAYING->{
                    stopPlaying()
                }
            }
        }
    }
    private fun initVariables(){
        state = State.BEFORE_RECORDING
    }
    private fun startRecordiong(){
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }
        recorder?.start()
        soundVisualizerView.startVisuallizing(false)
        recordTimeTextView.startCountUp()
        state = State.ON_RECORDING

    }
    private fun stopRecording(){
        recorder?.run{
            stop()
            release()
        }
        recorder=null
        soundVisualizerView.stopVisualizing()
        recordTimeTextView.startCountUp()
        state = State.AFTER_RECORDING

    }

    private fun startPlaying(){
        player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare()
        }
        player?.setOnCompletionListener {
            stopPlaying()
            state = State.AFTER_RECORDING
        }
        player?.start()
        soundVisualizerView.startVisuallizing(true)
        recordTimeTextView.startCountUp()
        state = State.ON_PLAYING
    }

    private fun stopPlaying(){
        player?.release()
        player = null
        soundVisualizerView.stopVisualizing()
        state = State.AFTER_RECORDING
    }

    companion object{
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}