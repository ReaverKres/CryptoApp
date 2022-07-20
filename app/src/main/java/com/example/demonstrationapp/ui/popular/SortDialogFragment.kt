package com.example.demonstrationapp.ui.popular

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult

class SortDialogFragment() : DialogFragment() {


    companion object {
        const val TAG = "SortDialogFragment"
        const val SORT_DIALOG_RESULT = "sortDialogResult"
        const val TYPE_OF_SORT = "typeOfSort"

        fun newInstance(typeOfSort: Int): SortDialogFragment = SortDialogFragment().apply {
            arguments = bundleOf(TYPE_OF_SORT to typeOfSort)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var typeOfSort = arguments?.getInt(TYPE_OF_SORT) ?: 0

        return AlertDialog.Builder(requireContext())
            .setSingleChoiceItems(
                arrayOf("По умолчанию", "По алфавиту", "По значению"),
                typeOfSort
            ) { _, pos ->
                typeOfSort = pos
            }
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                setFragmentResult(
                    SORT_DIALOG_RESULT,
                    bundleOf(TYPE_OF_SORT to typeOfSort)
                )
                dismiss()
            }
            .create()
    }
}