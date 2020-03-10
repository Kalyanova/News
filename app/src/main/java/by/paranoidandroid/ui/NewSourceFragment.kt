package by.paranoidandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import by.paranoidandroid.adapter.SourcesAdapter
import by.paranoidandroid.databinding.FragmentAddSourceBinding
import by.paranoidandroid.model.Source
import by.paranoidandroid.observeNonNull

class NewSourceFragment : Fragment(), SourcesAdapter.OnSourceItemClickListener {

    private lateinit var viewModel: NewSourceViewModel

    private var _binding: FragmentAddSourceBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddSourceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewSourceViewModel::class.java)
        viewModel.response.observeNonNull(viewLifecycleOwner) { sources ->
            binding.sourcesList.adapter = SourcesAdapter(sources, this)
        }
        viewModel.error.observeNonNull(viewLifecycleOwner) { message ->
            context?.let {
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.displaySources()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSourceItemClick(source: Source, isChecked: Boolean) {
        viewModel.updateSource(source, isChecked)
    }

    companion object {
        fun newInstance() = NewSourceFragment()
    }
}