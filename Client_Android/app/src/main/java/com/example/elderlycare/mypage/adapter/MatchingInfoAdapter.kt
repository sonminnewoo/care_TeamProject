package com.example.elderlycare.mypage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderlycare.R
import com.example.elderlycare.mypage.vo.MatchingDTO
import java.text.SimpleDateFormat
import java.util.Locale


class MatchingInfoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<MatchingDTO> = ArrayList<MatchingDTO>()
    fun setList(list: ArrayList<MatchingDTO>): Unit {
        this.list = list
        notifyDataSetChanged()
    }
    fun addMatInfo(dto: MatchingDTO) {list.add(dto); notifyDataSetChanged()}
    fun setMatInfo(position: Int, dto: MatchingDTO) {list.set(position, dto); notifyDataSetChanged()}
    fun getMatInfo(position: Int): MatchingDTO {return list.get(position)}

    inner class ProgressMatchingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvStartDate: TextView = view.findViewById(R.id.tvStartDate)
        val tvEndDate: TextView = view.findViewById(R.id.tvEndDate)
        val tvStartTime: TextView = view.findViewById(R.id.tvStartTime)
        val tvEndTime: TextView = view.findViewById(R.id.tvEndTime)
        val tvCareName: TextView = view.findViewById(R.id.tvWorkerName1)
        val tvCountry: TextView = view.findViewById(R.id.tvCountry)
        val tvResult: TextView = view.findViewById(R.id.tvResult1)

        fun bind(dto: MatchingDTO) {
            // 기존 날짜 형식 설정
            val originalFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
            val targetFormat = SimpleDateFormat("HH:mm", Locale.KOREA)

            // 시작 시간과 종료 시간에서 초 제거
            val startTime = originalFormat.parse(dto.startTime)
            val endTime = originalFormat.parse(dto.endTime)
            tvStartDate.text = dto.startDate
            tvEndDate.text = dto.endDate
            tvStartTime.text = targetFormat.format(startTime)
            tvEndTime.text = targetFormat.format(endTime)
            tvCareName.text = dto.matchingUserName
            tvCountry.text = dto.matchingCountry
            tvResult.text = dto.status
        }
    }

    inner class PastMatchingViewHolder(MatInfoView: View) : RecyclerView.ViewHolder(MatInfoView) {

        val tvStartDate: TextView = MatInfoView.findViewById(R.id.tvStartDate2)
        val tvEndDate: TextView = MatInfoView.findViewById(R.id.tvEndDate2)
        val tvStartTime: TextView = MatInfoView.findViewById(R.id.tvStartTime2)
        val tvEndTime: TextView = MatInfoView.findViewById(R.id.tvEndTime2)
        val tvCareName: TextView = MatInfoView.findViewById(R.id.tvWorkerName2)
        val tvCountry: TextView = MatInfoView.findViewById(R.id.tvCountry2)
        val tvResult: TextView = MatInfoView.findViewById(R.id.tvResult2)

        fun bind(dto: MatchingDTO){
            val originalFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
            val targetFormat = SimpleDateFormat("HH:mm", Locale.KOREA)

            val startTime = originalFormat.parse(dto.startTime)
            val endTime = originalFormat.parse(dto.endTime)
            tvStartDate.text = dto.startDate
            tvEndDate.text = dto.endDate
            tvStartTime.text = targetFormat.format(startTime)
            tvEndTime.text = targetFormat.format(endTime)
            tvCareName.text = dto.matchingUserName
            tvCountry.text = dto.matchingCountry
            tvResult.text = dto.status
        }
    }

    // 두 가지 뷰 타입을 정의
    companion object {
        const val TYPE_PAST = 0
        const val TYPE_PROGRESS = 1
    }

    // 각 항목의 뷰 타입을 결정하는 메소드를 오버라이드
    override fun getItemViewType(position: Int): Int {
        val matchingDTO = list[position]
        return if (matchingDTO.status == "COMPLETED"
            || matchingDTO.status == "CANCELLED") TYPE_PAST else TYPE_PROGRESS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_PAST -> {
                val pastLayout = inflater.inflate(R.layout.past_matching_row, parent, false)
                PastMatchingViewHolder(pastLayout)
            }
            TYPE_PROGRESS -> {
                val progressLayout = inflater.inflate(R.layout.progress_matching_row, parent, false)
                ProgressMatchingViewHolder(progressLayout)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val matchingDTO = list[position]
        when (holder) {
            is PastMatchingViewHolder -> holder.bind(matchingDTO)
            is ProgressMatchingViewHolder -> holder.bind(matchingDTO)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}