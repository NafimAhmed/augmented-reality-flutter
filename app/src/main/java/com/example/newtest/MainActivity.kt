package com.example.newtest

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class MainActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        // Set up tap listener
        arFragment.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, _ ->
            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING) return@setOnTapArPlaneListener

            // Create anchor at tapped location
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            // Load the 3D model
            ModelRenderable.builder()
                .setSource(this, Uri.parse("https://storage.googleapis.com/ar-answers-in-search-models/static/Tiger/model.glb")) // Ensure the model is in assets folder
                .build()
                .thenAccept { modelRenderable ->
                    val model = TransformableNode(arFragment.transformationSystem)
                    model.setParent(anchorNode)
                    model.renderable = modelRenderable
                    model.select()
                }
                .exceptionally { throwable ->
                    // Handle errors here
                    null
                }
        }
    }
}
