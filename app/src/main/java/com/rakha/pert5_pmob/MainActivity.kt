package com.rakha.pert5_pmob

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var rvStory: RecyclerView
    private lateinit var rvPost: RecyclerView
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter

    private val stories = mutableListOf<Story>()
    private val posts = mutableListOf<Post>()

    private var currentDialog: AlertDialog? = null
    private var selectedImageUri: Uri? = null
    private var selectedAddImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_ADD = 100
        private const val PICK_IMAGE_EDIT = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupData()
        setupRecyclerView()
        setupFab()
    }

    private fun initViews() {
        rvStory = findViewById(R.id.rvStory)
        rvPost = findViewById(R.id.rvPost)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    private fun setupData() {
        stories.apply {
            add(Story("rey_rakha", R.drawable.asset1))
            add(Story("itos", R.drawable.asset2))
            add(Story("klik_uad", R.drawable.asset3))
            add(Story("folkative", R.drawable.asset4))
            add(Story("dhiva", R.drawable.asset5))
            add(Story("udil", R.drawable.asset6))
            add(Story("uzli", R.drawable.asset1))
            add(Story("rakha", R.drawable.asset2))
        }

        posts.apply {
            posts.apply {
                add(Post("rey_rakha", "ngedit open gate Uad Job Fair", postImage = R.drawable.ngedit1, profileImage = R.drawable.ngedit1))
                add(Post("rey_rakha", "bikin logo uad job fair", postImage = R.drawable.ngedit2, profileImage = R.drawable.ngedit2))
                add(Post("rey_rakha", "hasil lembur", postImage = R.drawable.hasiledit, profileImage = R.drawable.hasiledit))
                add(Post("rey_rakha", "foto bareng setelah selesai acara", postImage = R.drawable.fotbar, profileImage = R.drawable.fotbar))
            }

        }
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter(stories)
        rvStory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvStory.adapter = storyAdapter

        postAdapter = PostAdapter(posts,
            onEditClick = { pos, post -> showEditDialog(pos, post) },
            onDeleteClick = { pos -> deletePostWithConfirm(pos) }
        )
        rvPost.layoutManager = LinearLayoutManager(this)
        rvPost.adapter = postAdapter
    }

    private fun setupFab() {
        val fabAddPost: FloatingActionButton = findViewById(R.id.fabAddPost)
        fabAddPost.setOnClickListener { showAddPostDialog() }
    }

    // bagian edit postingan
    private fun showEditDialog(position: Int, post: Post) {
        selectedImageUri = null
        val dialogView = LayoutInflater.from(this).inflate(R.layout.edit_post, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        currentDialog = dialog

        val etUsername = dialogView.findViewById<EditText>(R.id.etAddUsername)
        val etCaption = dialogView.findViewById<EditText>(R.id.etAddCaption)
        val btnSelectImage = dialogView.findViewById<Button>(R.id.btnAddSelectImage)
        val ivImagePreview = dialogView.findViewById<ImageView>(R.id.ivAddImagePreview)
        val btnSave = dialogView.findViewById<Button>(R.id.btnAddSave)

        etUsername.setText(post.username)
        etCaption.setText(post.caption)

        if (post.imageUri != null) {
            ivImagePreview.setImageURI(Uri.parse(post.imageUri))
            ivImagePreview.visibility = View.VISIBLE
        } else if (post.postImage != 0) {
            ivImagePreview.setImageResource(post.postImage)
            ivImagePreview.visibility = View.VISIBLE
        }

        btnSelectImage.setOnClickListener { openGallery(isEdit = true) }
        btnSave.setOnClickListener {
            val newUsername = etUsername.text.toString().trim()
            val newCaption = etCaption.text.toString().trim()
            if (newUsername.isEmpty() || newCaption.isEmpty()) {
                Toast.makeText(this, "Username dan caption tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            postAdapter.updatePost(
                position,
                post.copy(
                    username = newUsername,
                    caption = newCaption,
                    imageUri = selectedImageUri?.toString() ?: post.imageUri
                )
            )

            Toast.makeText(this, "postingan $newUsername berhasil diperbarui", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    // bagian nambahin postingan
    private fun showAddPostDialog() {
        selectedAddImageUri = null
        val dialogView = LayoutInflater.from(this).inflate(R.layout.new_post, null)
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        currentDialog = dialog

        val etUsername = dialogView.findViewById<EditText>(R.id.etAddUsername)
        val etCaption = dialogView.findViewById<EditText>(R.id.etAddCaption)
        val btnSelectImage = dialogView.findViewById<Button>(R.id.btnAddSelectImage)
        val ivImagePreview = dialogView.findViewById<ImageView>(R.id.ivAddImagePreview)
        val btnSave = dialogView.findViewById<Button>(R.id.btnAddSave)
        ivImagePreview.visibility = View.GONE

        btnSelectImage.setOnClickListener {
            openGallery(isEdit = false)
        }

        btnSave.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val caption = etCaption.text.toString().trim()

            if (username.isEmpty() || caption.isEmpty() || selectedAddImageUri == null) {
                Toast.makeText(this, "Harap isi semua field dan pilih gambar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPost = Post(
                username = username,
                caption = caption,
                imageUri = selectedAddImageUri.toString()
            )
            posts.add(0, newPost)
            postAdapter.notifyItemInserted(0)
            rvPost.scrollToPosition(0)

            Toast.makeText(this, "postingan $username berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            selectedAddImageUri = null
        }

        dialog.show()
    }
    // bagian hapus postingan
    private fun deletePostWithConfirm(position: Int) {
        val name = postAdapter.getPostUsername(position)
        postAdapter.deletePost(position)
        Toast.makeText(this, "postingan $name berhasil dihapus ️", Toast.LENGTH_SHORT).show()
    }

    // bagian buka galeri
    private fun openGallery(isEdit: Boolean) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, if (isEdit) PICK_IMAGE_EDIT else PICK_IMAGE_ADD)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val uri = data.data
            when (requestCode) {
                PICK_IMAGE_ADD -> {
                    selectedAddImageUri = uri
                    currentDialog?.findViewById<ImageView>(R.id.ivAddImagePreview)?.let { imageView ->
                        imageView.setImageURI(uri)
                        imageView.visibility = View.VISIBLE
                    }
                    Toast.makeText(this, "Gambar dipilih", Toast.LENGTH_SHORT).show()
                }
                PICK_IMAGE_EDIT -> {
                    selectedImageUri = uri
                    currentDialog?.findViewById<ImageView>(R.id.ivAddImagePreview)?.let { imageView ->
                        imageView.setImageURI(uri)
                        imageView.visibility = View.VISIBLE
                    }
                    Toast.makeText(this, "Gambar dipilih", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
        }
    }
}