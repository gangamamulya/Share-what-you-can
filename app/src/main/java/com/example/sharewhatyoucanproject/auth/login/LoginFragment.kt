package com.example.sharewhatyoucanproject.auth.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sharewhatyoucanproject.databinding.FragmentLoginBinding
import com.example.sharewhatyoucanproject.models.UserType
import com.example.sharewhatyoucanproject.utils.editSharedPreferencesSelector
import com.example.sharewhatyoucanproject.utils.getUserType
import com.example.sharewhatyoucanproject.utils.showToast

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginViewModel: LoginViewModel

    private val args: LoginFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity?)?.supportActionBar?.hide()

        loginViewModel.type = getUserType(args)

        loginViewModel.selectorResult.observe(viewLifecycleOwner) { type ->
            type?.let {
                editSharedPreferencesSelector(requireContext(), type)
            }
        }
        loginViewModel.authenticationResult.observe(requireActivity()) { result ->
            when (result) {
                is AuthenticationResult.Fail -> {
                    binding.progressCircular.visibility = GONE
                    requireContext().showToast(result.message)
                }
                is AuthenticationResult.LoginSuccess -> {
                    binding.progressCircular.visibility = GONE
                    val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
                is AuthenticationResult.SignUpSuccess -> {
                    requireContext().showToast("Created")
                }

                is AuthenticationResult.SaveDataSuccess -> {
                    val action = if (loginViewModel.type == UserType.DONOR) {
                        LoginFragmentDirections.actionLoginFragmentToSelectUserFragment()
                    } else {
                        LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    }
                    findNavController().navigate(action)
                }
            }
        }

        if (loginViewModel.type == UserType.DONOR) {
            binding.mytv.text = "Donor"
        } else if (loginViewModel.type == UserType.RECEIVER) {
            binding.mytv.text = "Receiver"
        }

        binding.submitdevice.setOnClickListener {
            val deviceId = Settings.Secure.getString(
                requireActivity().contentResolver,
                Settings.Secure.ANDROID_ID,
            )
            loginViewModel.deviceId = deviceId
            binding.progressCircular.visibility = VISIBLE
            loginViewModel.checkUser(deviceId)
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
