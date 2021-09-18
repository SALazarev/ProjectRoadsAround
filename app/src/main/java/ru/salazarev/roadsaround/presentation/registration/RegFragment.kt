package ru.salazarev.roadsaround.presentation.registration

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.databinding.FragmentRegBinding
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.toast
import javax.inject.Inject

class RegFragment : Fragment() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val IMAGE_KEY = "data"
    }

    /** Фабрика для ViewModel текущего фрагмента */
    @Inject
    lateinit var regViewModelFactory: RegViewModelFactory

    private var _binding: FragmentRegBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RegViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.appComponent.getMainComponentBuilder().fragmentManager(childFragmentManager).build()
            .inject(this)
        viewModel = ViewModelProvider(this, regViewModelFactory).get(RegViewModel::class.java)
        _binding = FragmentRegBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        binding.btnUserPhoto.setOnClickListener { createPhoto() }
        setObservers()
    }

    private fun setObservers() {
        viewModel.result.observe(viewLifecycleOwner, { resultStatus ->
            if (resultStatus) {
                (activity as MainActivity)
                    .navController
                    .navigate(R.id.action_regFragment_to_mainFragment)
            } else {
                requireActivity().toast(getString(R.string.reg_unsuccessful))
            }
        })
        viewModel.progress.observe(viewLifecycleOwner, { loadStatus ->
            if (loadStatus) binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.INVISIBLE
        })
    }

    private fun setToolbar() {
        binding.includeToolbar.includeToolbar.apply {
            inflateMenu(R.menu.toolbar_reg_menu)
            title = context.getString(R.string.registration)
            navigationContentDescription = context.getString(R.string.back)
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.outline_arrow_back_24)
            setNavigationOnClickListener {requireActivity().onBackPressed()}
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_login -> {
                        registration()
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }

        }
    }

    private fun createPhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            requireActivity().toast(getString(R.string.failed_launch_camera_app))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get(IMAGE_KEY) as Bitmap
            binding.btnUserPhoto.background = BitmapDrawable(resources, imageBitmap)
            viewModel.buffImage = imageBitmap
        }
    }

    private fun registration() {
        val email = binding.textInputEmail.text.toString()
        val password = binding.textInputPass.text.toString()
        val firstName = binding.textInputFirstName.text.toString()

        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty()) {
            context?.toast(getString(R.string.required_data_fields_empty))
            return
        } else {
            viewModel.registrationUser(
                email,
                password,
                firstName,
                binding.etLastName.editText!!.text.toString(),
                viewModel.buffImage
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}