package com.example.elderlycare.notice.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderlycare.R
import com.example.elderlycare.notice.model.Notice
import com.example.elderlycare.notice.retrofit.RetrofitClient
import com.example.elderlycare.notice.view.NoticeDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class NoticeAdapter(
    private val context: Context,
    private var noticeList: List<Notice>
) : RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    // 기존 noticeList를 업데이트하는 메서드 추가
    fun updateNoticeList(newNoticeList: List<Notice>) {
        noticeList = newNoticeList
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val titleTextView: TextView = itemView.findViewById(R.id.recyclerTitle_view)
        private val writerTextView: TextView = itemView.findViewById(R.id.recyclerWriter_view)
        private val regDateTextView: TextView = itemView.findViewById(R.id.recyclerRegdate_view)

        init {
            // ViewHolder의 클릭 이벤트를 설정합니다.
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            // 클릭한 공지사항의 ID를 가져옵니다.
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val notice = noticeList[position]
                val noticeNum = notice.num

                // NoticeDetailActivity로 이동하여 클릭한 공지사항의 ID를 전달합니다.
                val intent = Intent(context, NoticeDetailActivity::class.java)
                intent.putExtra("notice_num", noticeNum)
                context.startActivity(intent)
            }
        }

        fun bind(notice: Notice) {
            titleTextView.text = notice.title
            writerTextView.text = notice.writer
            // 날짜 형식을 변환하여 출력합니다.
            val regDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault()).parse(notice.regDate)
            val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(regDate)
            regDateTextView.text = formattedDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_notice_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notice = noticeList[position]
        holder.bind(notice)
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    fun loadNotice() {
        val service = RetrofitClient.noticeService
        val call = service.getAllNotices()

        call.enqueue(object : Callback<List<Notice>> {
            override fun onResponse(call: Call<List<Notice>>, response: Response<List<Notice>>) {
                if (response.isSuccessful) {
                    val notices = response.body() ?: emptyList()
                        noticeList = notices
                        notifyDataSetChanged()

                } else {
                    // 서버로부터 공지사항을 가져오지 못했을 때의 처리
                    // 예: 오류 메시지 표시 등
                }
            }

            override fun onFailure(call: Call<List<Notice>>, t: Throwable) {
                // 서버 통신 실패 시의 처리
                // 예: 네트워크 오류 메시지 표시 등
            }
        })
    }
}
