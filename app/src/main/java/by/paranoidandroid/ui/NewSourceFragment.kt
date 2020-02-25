package by.paranoidandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import by.paranoidandroid.R

class NewSourceFragment : Fragment() {

    private lateinit var viewModel: NewSourceViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_source, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewSourceViewModel::class.java)
        viewModel.addNewSource()
    }

    companion object {
        fun newInstance() = NewSourceFragment()
    }
}