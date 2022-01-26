package id.ditha.pcs.di

import id.ditha.pcs.ui.login.AuthViewModel
import id.ditha.pcs.ui.product.ProductViewModel
import id.ditha.pcs.ui.report.ReportViewModel
import id.ditha.pcs.ui.transaction.TransactionViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { ReportViewModel(get()) }
    viewModel { TransactionViewModel(get()) }
}