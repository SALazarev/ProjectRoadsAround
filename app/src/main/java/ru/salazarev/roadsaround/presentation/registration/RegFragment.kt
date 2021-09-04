package ru.salazarev.roadsaround.presentation.registration

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentRegBinding
import ru.salazarev.roadsaround.di.DaggerAppComponent
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.toast
import javax.inject.Inject


class RegFragment : Fragment() {

    private var _binding: FragmentRegBinding? = null
    private val binding get() = _binding!!

    private lateinit var model: RegViewModel

    @Inject
    lateinit var regViewModelFactory: RegViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        DaggerAppComponent.builder().fragmentManager(childFragmentManager).build().inject(this)
        model = ViewModelProvider(this, regViewModelFactory).get(RegViewModel::class.java)
        _binding = FragmentRegBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_reg_menu)
            title = context.getString(R.string.registration)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_back_24)
            setNavigationOnClickListener { (activity as MainActivity).navController.navigate(R.id.action_regFragment_to_authFragment) }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_login -> {
                        registration()
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
            binding.btnUserPhoto.setOnClickListener { createPhoto() }
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1
    private fun createPhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.btnUserPhoto.background = BitmapDrawable(resources, imageBitmap)
            model.image = imageBitmap
        }
    }

    private fun registration() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPass.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            context?.toast(getString(R.string.required_data_fields_empty))
            return
        } else {
            (activity as MainActivity).fireBaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        context?.toast(getString(R.string.reg_successful))

                        model.saveData(
                            binding.etFirstName.text.toString(),
                            binding.etLastName.text.toString()
                        )

                        (activity as MainActivity).navController.navigate(R.id.action_regFragment_to_mainFragment)
                    } else {
                        context?.toast(getString(R.string.reg_unsuccessful))
                    }
                }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}