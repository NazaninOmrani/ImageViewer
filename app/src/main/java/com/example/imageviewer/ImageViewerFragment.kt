package com.example.customeimageviewer

import android.animation.LayoutTransition
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.example.imageviewer.R
import com.example.imageviewer.util.LayoutHelper
import de.hdodenhof.circleimageview.CircleImageView

class ImageViewerFragment : Fragment() {

    private lateinit var rootView: FrameLayout
    private lateinit var zoomLayout: ZoomLayout
    private lateinit var imageView: ImageView
    private lateinit var bottomRootView: FrameLayout
    private lateinit var circleImageView: CircleImageView
    private lateinit var textViewDescription: TextView
    private lateinit var textViewName: TextView
    private lateinit var textViewDate: TextView
    private lateinit var imageViewForward: ImageView
    private lateinit var imageViewShare: ImageView
    private var isReadMore: Boolean = false
    private lateinit var verticalSwipeLayout: VerticalSwipeLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = FrameLayout(requireContext())
        rootView.id = R.id.photoViewerRootView
        rootView.setBackgroundColor(Color.BLACK)
        verticalSwipeLayout = context?.let { VerticalSwipeLayout(it) }!!

        zoomLayout = context?.let { ZoomLayout(it) }!!
        zoomLayout.id = R.id.photoViewerZoomLayout
        imageView = ImageView(requireContext())
        imageView.setBackgroundColor(Color.WHITE)
        imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
        imageView.id = R.id.photoViewerImage
        zoomLayout.addView(
            imageView,
            LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT,
                300,
                Gravity.CENTER
            )
        )

        rootView.addView(
            zoomLayout,
            LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT,
                LayoutHelper.MATCH_PARENT,
                Gravity.CENTER
            )
        )

        bottomRootView = FrameLayout(requireContext())
        bottomRootView.id = R.id.photoViewerBottomRootView
        val transition = LayoutTransition()
        transition.setDuration(500)
        transition.enableTransitionType(LayoutTransition.CHANGING)
        bottomRootView.layoutTransition = transition
        bottomRootView.background = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.shape_transparent_background
        )

        rootView.addView(
            bottomRootView, LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT,
                LayoutHelper.WRAP_CONTENT,
                Gravity.BOTTOM
            )
        )

        textViewDescription = TextView(requireContext())
        textViewDescription.id = R.id.photoViewerDescription
        textViewDescription.gravity = Gravity.START
        textViewDescription.text =
            "Buy Digital Gift Card Request Card to Card Buying The Charge Buying Internet Package to Card Buying The Charge Buying Internet Package to Card Buying The Charge Buying Internet Package Buy Digital Gift Card Request Card to Card Buying The Charge Buying Internet Package to Card Buying The Charge Buying Internet Package to Card Buying The Charge Buying Internet Package Buy Digital Gift Card Request Card to Card Buying The Charge Buying Internet Package to Card Buying The Charge Buying Internet Package to Card Buying The Charge Buying Internet Package Buy Digital Gift Card Request Card to Card Buying The Charge Buying Internet Package to Card Buying The Charge Buying "
        textViewDescription.textSize = 16F
        textViewDescription.isSingleLine = false
        textViewDescription.ellipsize = TextUtils.TruncateAt.END
        textViewDescription.maxLines = 4
        textViewDescription.setTextColor(Color.WHITE)
        textViewDescription.setOnClickListener { onDescriptionClicked() }
        bottomRootView.addView(
            textViewDescription, LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT,
                LayoutHelper.WRAP_CONTENT,
                Gravity.TOP,
                24,
                22,
                24,
                66
            )
        )

        circleImageView = CircleImageView(context)
        circleImageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
        circleImageView.setColorFilter(Color.WHITE)
        circleImageView.id = R.id.photoViewerCircleImage
        bottomRootView.addView(
            circleImageView, LayoutHelper.createFrame(
                40,
                40,
                Gravity.LEFT or Gravity.BOTTOM,
                24,
                10,
                0,
                17
            )
        )

        textViewName = TextView(requireContext())
        textViewName.id = R.id.photoViewerName
        textViewName.text = "Name"
        textViewName.textSize = 16F
        textViewName.gravity = Gravity.START
        textViewName.maxLines = 1
        textViewName.isSingleLine = true
        textViewName.setTextColor(Color.WHITE)
        bottomRootView.addView(
            textViewName, LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT,
                Gravity.START or Gravity.BOTTOM,
                76,
                14,
                0,
                30
            )
        )

        textViewDate = TextView(requireContext())
        textViewDate.id = R.id.photoViewerDate
        textViewDate.gravity = Gravity.START
        textViewDate.text = "22.05.2022 | 20:54"
        textViewDate.textSize = 9F
        textViewDate.maxLines = 1
        textViewDate.isSingleLine = true
        textViewDate.setTextColor(Color.WHITE)
        bottomRootView.addView(
            textViewDate, LayoutHelper.createFrame(
                LayoutHelper.MATCH_PARENT,
                LayoutHelper.WRAP_CONTENT,
                Gravity.START or Gravity.BOTTOM,
                76,
                34,
                0,
                20
            )
        )

        imageViewForward = ImageView(requireContext())
        imageViewForward.setImageResource(R.drawable.ic_baseline_forward_24)
        imageViewForward.id = R.id.photoViewerForwardIcon
        bottomRootView.addView(
            imageViewForward,
            LayoutHelper.createFrame(
                20,
                20,
                Gravity.END or Gravity.BOTTOM,
                0,
                22,
                57,
                24
            )
        )

        imageViewShare = ImageView(requireContext())
        imageViewShare.setImageResource(R.drawable.ic_baseline_share_24)
        imageViewShare.id = R.id.photoViewerShareIcon
        bottomRootView.addView(
            imageViewShare,
            LayoutHelper.createFrame(
                20,
                20,
                Gravity.END or Gravity.BOTTOM,
                0,
                22,
                24,
                24
            )
        )

        return verticalSwipeLayout.setFragment(this, rootView)
    }

    private fun onDescriptionClicked() {
        isReadMore = !isReadMore
        if (isReadMore) {
            textViewDescription.isVerticalScrollBarEnabled = false
            textViewDescription.maxLines = Integer.MAX_VALUE
        } else {
            textViewDescription.ellipsize = TextUtils.TruncateAt.END
            textViewDescription.maxLines = 4
        }
    }
}