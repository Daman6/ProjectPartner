package com.example.projectpartner.registerfragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.example.projectpartner.R
import com.example.projectpartner.databinding.FragmentTwoBinding
import java.util.*


class FragmentTwo : Fragment() , DatePickerDialog.OnDateSetListener {

    var day = 0
    var month = 0
    var year = 0
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0

    lateinit var binding: FragmentTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun getDateCalenedar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    fun pickDate(){
            getDateCalenedar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
    }
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        savedDay= p3
        savedMonth= p2
        savedYear= p1
        val result = "$savedDay/$savedMonth/$savedYear"
        binding.bdayEt.setText(result)
    }

}