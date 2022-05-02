package com.bignerdranch.android.criminalintent

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var crimeRecyclerView: RecyclerView
    private lateinit var adapter: CrimeAdapter

    @SuppressLint("SimpleDateFormat")
    private val dateFormatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss EEEE")

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView =
            view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        navController = findNavController()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CrimeAdapter()
        crimeRecyclerView.adapter = adapter
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner
        ) {
            it?.let {
                Log.i(TAG, "Got crimes ${it.size}")
                adapter.submitList(it)
            }
        }
    }

    inner class CrimeAdapter : ListAdapter<Crime, CrimeViewHolder>(CrimeDiffCallback()) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CrimeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeViewHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeViewHolder, position: Int) {
            holder.bind(getItem(position))
        }
    }

    inner class CrimeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private lateinit var crime: Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedCrimeImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val bundle: Bundle = bundleOf(ARG_CRIME_ID to crime.id)
            navController.navigate(R.id.action_crimeListFragment_to_crimeFragment, bundle)
        }

        fun bind(crime: Crime?) {
            if (crime is Crime) {
                this.crime = crime
                titleTextView.text = crime.title
                dateTextView.text = dateFormatter.format(crime.date)
                solvedCrimeImageView.visibility = if (!crime.isSolved) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    companion object {
        const val ARG_CRIME_ID = "crime_id"
    }
}


private class CrimeDiffCallback : DiffUtil.ItemCallback<Crime>() {
    override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
        return oldItem == newItem
    }
}


