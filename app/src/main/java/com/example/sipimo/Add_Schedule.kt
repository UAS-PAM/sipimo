package com.example.sipimo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Add_Schedule.newInstance] factory method to
 * create an instance of this fragment.
 */
class Add_Schedule : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var diseaseEditText: EditText
    private lateinit var medicineEditText: EditText
    private lateinit var spinnerDrugtype: Spinner
    private lateinit var amountDrugEditText: EditText
    private lateinit var usedEditText: EditText
    private lateinit var spinnerDuring: Spinner
    private lateinit var spinnerStart: Spinner
    private lateinit var saveBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add__schedule, container, false)

        diseaseEditText = view.findViewById(R.id.etDiseaseName)
        medicineEditText = view.findViewById(R.id.etMedicineName)
        spinnerDrugtype = view.findViewById(R.id.spinnerDrugType)
        amountDrugEditText = view.findViewById(R.id.etAmountDrug)
        usedEditText = view.findViewById(R.id.etUsed)
        spinnerDuring = view.findViewById(R.id.spinnerDuring)
        spinnerStart = view.findViewById(R.id.spinnerStart)
        saveBtn = view.findViewById(R.id.saveBtn)
        cancelBtn = view.findViewById(R.id.cancelBtn)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://sipimo-pam-default-rtdb.asia-southeast1.firebasedatabase.app/")
        myRef = database.getReference("schedules")

        // Create Array Adapter
        createAdapter(R.array.drugtype_array, spinnerDrugtype)
        createAdapter(R.array.duringtype_array, spinnerDuring)
        createAdapter(R.array.start_array, spinnerStart)

        saveBtn.setOnClickListener{
            val diseaseName = diseaseEditText.text.toString().trim()
            val medicineName = medicineEditText.text.toString().trim()
            val drugType = spinnerDrugtype.selectedItem.toString().trim()
            val amountDrugStr = amountDrugEditText.text.toString().trim()
            val usedStr = usedEditText.text.toString().trim()
            val during = spinnerDuring.selectedItem.toString().trim()
            val start = spinnerStart.selectedItem.toString().trim()

            if (
                diseaseName.isEmpty() ||
                medicineName.isEmpty() ||
                drugType == "Select item" ||
                amountDrugStr.isEmpty() ||
                usedStr.isEmpty() ||
                during == "Select item" ||
                start == "Select item"
            ) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amountDrug: Int
            val used: Int

            try {
                amountDrug = amountDrugStr.toInt()
                used = usedStr.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Please enter valid numbers for amount and used fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (amountDrug == 0 || used == 0) {
                Toast.makeText(requireContext(), "Amount and used fields must be greater than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid
            val scheduleId = generateUniqueID()

            if (userId == null) {
                Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val schedule = Schedule(userId, diseaseName, medicineName, drugType, amountDrug, used, during, start)

            myRef.child(scheduleId).setValue(schedule).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Schedule Successfully Added", Toast.LENGTH_SHORT).show()
                    // Clear all EditText fields
                    diseaseEditText.text.clear()
                    medicineEditText.text.clear()
                    amountDrugEditText.text.clear()
                    usedEditText.text.clear()

                    // Reset all Spinners to their default value
                    spinnerDrugtype.setSelection(0)
                    spinnerDuring.setSelection(0)
                    spinnerStart.setSelection(0)
                } else {
                    Toast.makeText(requireContext(), "Failed To Add Schedule", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view

    }

    fun generateUniqueID(): String {
        return UUID.randomUUID().toString()
    }

    private fun createAdapter(adapter: Int, spinner: Spinner) {
        ArrayAdapter.createFromResource(requireContext(),
            adapter,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Add_Schedule.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Add_Schedule().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}