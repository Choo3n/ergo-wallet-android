package org.ergoplatform.android.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.ergoplatform.android.databinding.FragmentChooseTokenDialogBinding
import org.ergoplatform.android.databinding.FragmentChooseTokenDialogItemBinding
import org.ergoplatform.android.wallet.WalletTokenDbEntity

/**
 * Let the user choose one or more token(s) from the available tokens
 */
class ChooseTokenListDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChooseTokenDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChooseTokenDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.list.layoutManager =
            LinearLayoutManager(context)

        val viewModel =
            ViewModelProvider(parentFragment as ViewModelStoreOwner)
                .get(SendFundsViewModel::class.java)
        val tokensToChooseFrom = viewModel.getTokensToChooseFrom()
        binding.list.adapter = DisplayTokenAdapter(tokensToChooseFrom)
    }

    private fun onChooseToken(tokenId: String) {
        ViewModelProvider(parentFragment as ViewModelStoreOwner).get(SendFundsViewModel::class.java)
            .newTokenChoosen(tokenId)
        dismiss()
    }

    private inner class ViewHolder internal constructor(binding: FragmentChooseTokenDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal val name: TextView = binding.labelTokenName
    }

    private inner class DisplayTokenAdapter internal constructor(private val items: List<WalletTokenDbEntity>) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                FragmentChooseTokenDialogItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val token = items.get(position)
            holder.name.text = token.name
            holder.name.setOnClickListener { onChooseToken(token.tokenId!!) }
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}