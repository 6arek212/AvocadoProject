package com.example.testavocado.ccc

import android.animation.ObjectAnimator
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testavocado.R
import com.example.testavocado.Utils.TimeMethods
import android.R.attr.button
import android.net.Uri
import android.view.animation.Animation
import android.view.animation.AlphaAnimation


@BindingAdapter("numBind")
fun numBind(view: TextView, number: Int?) {
    if (number == 0 || number == null) {
        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.duration = 1000
        anim.repeatCount = 0
        anim.repeatMode = Animation.REVERSE
        view.startAnimation(anim)
        view.visibility = View.GONE
    }
    else {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1000
        anim.repeatCount = 0
        anim.repeatMode = Animation.REVERSE
        view.startAnimation(anim)
        view.text = number.toString()
        view.visibility = View.VISIBLE
    }
}



@BindingAdapter("imageUri")
fun bindImageUri(imgView: ImageView, imgUrl: Uri?) {
    Log.d("BindingAdapter", "${imgUrl}")

    Glide.with(imgView.context)
            .load(imgUrl)
            .centerCrop()
            .apply(
                    RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.profile_ic)
            )
            .into(imgView)
}


@BindingAdapter("setOnlineState")
fun setOnlineState(imgView: ImageView, state: Boolean?) {

    state?.let {
        when (state) {
            true -> imgView.setImageDrawable(imgView.context.getDrawable(R.drawable.greencircle))
            false -> imgView.setImageDrawable(imgView.context.getDrawable(R.drawable.circle_grey))
        }
    }
}


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Log.d("BindingAdapter", "${imgUrl}")

    Glide.with(imgView.context)
            .load(imgUrl)
            .centerCrop()
            .apply(
                    RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.profile_ic)
            )
            .into(imgView)


}

@BindingAdapter("time")
fun time(view: TextView, time: String?) {
    time?.let {
        view.text = TimeMethods.convertDateTimeFormat2(time)
    }
}


@BindingAdapter("progressBarState")
fun time(view: View, state: Boolean?) {
    state?.let {
        when (it) {
            true -> {
                val anim = AlphaAnimation(0.0f, 1.0f)
                anim.duration = 1000
                anim.repeatCount = 0
                anim.repeatMode = Animation.REVERSE
                view.startAnimation(anim)

                view.visibility = View.VISIBLE
            }
            false -> {
                val anim = AlphaAnimation(1.0f, 0.0f)
                anim.duration = 1000
                anim.repeatCount = 0
                anim.repeatMode = Animation.REVERSE
                view.startAnimation(anim)
                view.visibility = View.GONE
            }
        }


    }
}