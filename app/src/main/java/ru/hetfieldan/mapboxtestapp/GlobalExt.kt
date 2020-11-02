package ru.hetfieldan.mapboxtestapp

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.MainThread
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.IOException

fun Any.mylog(message: Any?) {
    val className = this::class.java.simpleName
    Log.e("MYLOG $className", message?.toString() ?: "message is null")
}

fun allPermissionsGranted(grantResults: IntArray): Boolean {
    if (grantResults.isEmpty()) return false
    else {
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }
}

fun AppCompatActivity.showSettingsIntentDialog(permissionString: String) {
    AlertDialog.Builder(this)
        .setTitle(R.string.settings_intent_label)
        .setMessage(permissionString)
        .setPositiveButton(R.string.ok) { _, _ ->
            startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
        }
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        .create()
        .show()
}

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun <T : DialogFragment> T.putArgs(argsBuilder: Bundle.() -> Unit): T {
    return this.apply {
        arguments = Bundle().apply(argsBuilder)
    }
}

inline fun SpannableStringBuilder.inSpans(
    vararg spans: Any,
    builderAction: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    val start = length
    builderAction()
    for (span in spans) setSpan(span, start, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    return this
}

inline fun SpannableStringBuilder.bold(builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(StyleSpan(Typeface.BOLD), builderAction = builderAction)

fun makeStringFromLabelAndValue(label: String, value: String): SpannableStringBuilder {
    return SpannableStringBuilder().apply {
        bold { append("$label: ") }
        append(value)
    }
}

inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(
    viewModelFactory: ViewModelProvider.Factory
): T {
    return ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
}

fun Disposable.addTo(androidDisposable: CompositeDisposable): Disposable = apply { androidDisposable.add(this) }

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}
