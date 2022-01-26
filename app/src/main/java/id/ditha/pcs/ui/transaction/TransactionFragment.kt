package id.ditha.pcs.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.ditha.pcs.MyApp.Companion.prefManager
import id.ditha.pcs.databinding.FragmentTransactionBinding
import id.ditha.pcs.ui.adapter.TransactionAdapter
import id.ditha.pcs.ui.product.ProductViewModel
import id.ditha.pcs.utils.*
import org.koin.android.viewmodel.ext.android.viewModel

class TransactionFragment : Fragment(), OnItemClicked {

    private val viewModel: ProductViewModel by viewModel()
    lateinit var adapter: TransactionAdapter

    var totalPrice = 0

    private var _fragment: FragmentTransactionBinding? = null
    private val binding get() = _fragment as FragmentTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _fragment = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeData()
    }

    private fun initView() {
        adapter = TransactionAdapter(this)

        with(binding.rvProduct){
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TransactionFragment.adapter
        }

        viewModel.getProduct()

        with(binding) {
            btnAdd.setOnClickListener {
                prefManager.spTotalPrice = totalPrice
                TransactionPayDialog.build() {}.show(requireFragmentManager(), tag(requireContext()))
            }
        }
    }

    private fun observeData() {
        viewModel.state.observe(viewLifecycleOwner, { result ->
            when (result) {
                is ProductViewModel.ProductState.Succes -> {
                    this.adapter.addList(result.product)
                    binding.let {
                        it.progress.hide()
                        //if empty
                        if (result.product.isNullOrEmpty()) {
                            activity?.toast("data kosong")
                            it.progress.hide()
                        }
                    }
                }

                is ProductViewModel.ProductState.Error -> {
                    binding.let {
                        activity?.toast(result.message)
                        it.progress.show()
                    }
                }
                is ProductViewModel.ProductState.Loading -> {
                    binding.progress.show()
                }
            }
        })
    }

    override fun onIncreaseTransaction(total: Int, position: Int) {
        super.onIncreaseTransaction(total, position)

        totalPrice += (adapter.list[position].harga!!.toInt() * total)
        binding.tvTotal.text = "Rp. $totalPrice"
    }

    override fun onDecreaseTransaction(total: Int, position: Int) {
        super.onDecreaseTransaction(total, position)

        totalPrice -= (adapter.list[position].harga!!.toInt() * total)
        binding.tvTotal.text = "Rp. $totalPrice"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragment = null
    }
}