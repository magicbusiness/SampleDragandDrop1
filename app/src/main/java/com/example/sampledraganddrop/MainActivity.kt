package com.example.sampledraganddrop

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.textView)
        val textView2: TextView = findViewById(R.id.textView2)

        // Initialize Random
        val randomNumber = Random().nextInt(100)

        textView.text = randomNumber.toString()
        textView.tag = textView.text

        textView.setOnClickListener {
            textView.text = randomNumber.toString()
            textView.tag = textView.text
        }

//
//        textView.setOnClickListener { v ->
//            val randomNumber = Random().nextInt(100)
//            (v as TextView).text = "$randomNumber"
//            (v as TextView).tag = (v as TextView).text
//        }

        textView.setOnLongClickListener {
            val clipText = "This is Clip Text"
            val item = ClipData.Item(clipText)
            val data = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)

            val dragData = ClipData(clipText, data, item)

            val dragShadow = View.DragShadowBuilder(it)
            it.startDragAndDrop(dragData, dragShadow, it, 0)

            it.visibility = View.INVISIBLE

            true

            // SDK 24 Bellow
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                v.startDragAndDrop(dragData, myShadow, null, 0)
//            } else {
//                v.startDrag(dragData, myShadow, null, 0)
//            }
        }


        textView2.setOnDragListener { v, event ->
            val receiverView:TextView = v as TextView

            when(event.action)
            {
                DragEvent.ACTION_DRAG_STARTED -> {
                    receiverView.setBackgroundColor(Color.CYAN)
                    receiverView.text = "Hold and drag here."

                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.invalidate()

                    receiverView.setBackgroundColor(Color.GREEN)
                    receiverView.text = "Good, put here."

                    true
                }

                DragEvent.ACTION_DRAG_LOCATION -> true

                DragEvent.ACTION_DRAG_EXITED -> {
                    v.invalidate()

                    receiverView.setBackgroundColor(Color.YELLOW)
                    receiverView.text = "Oh! you exited."

                    true
                }

                DragEvent.ACTION_DROP -> {
                    val item = event.clipData.getItemAt(0)
                    val dropData = item.text

                    val dragData = item.text
                    receiverView.text = "You dropped : $dragData"

                    Toast.makeText(this, dropData, Toast.LENGTH_SHORT).show()

                    val look = event.localState as View

                    val owner = look.parent as ViewGroup
                    owner.removeView(look)

                    look.visibility = View.VISIBLE
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    v.invalidate()

                    receiverView.setBackgroundColor(Color.WHITE)

                    when(event.result) {
                        true ->
                            // drop was handled
                            receiverView.setBackgroundColor(Color.BLACK)
                        else ->{
                            // drop didn't work
                            receiverView.text = "Drop failed."
                            receiverView.setBackgroundColor(Color.RED)
                        }
                    }

                    // returns true; the value is ignored.
                    true
                }

                else -> false
            }
        }
    }
}