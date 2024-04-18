package com.example.elderlycare.board.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderlycare.R
import com.example.elderlycare.board.vo.BoardVO


class BoardAdapter : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() { // ':'는 상속
    // ViewHolder: 데이터를 담을 데이터 리스트 목록 하나에 해당되는 틀

    // 데이터를 담기 위한 list
    private var list: ArrayList<BoardVO> = ArrayList<BoardVO>()
    fun setList(list: ArrayList<BoardVO>): Unit { this.list = list }
    fun addBoard(vo: BoardVO) {list.add(vo); notifyDataSetChanged()}   // list이기 때문에 add 사용해야 함
    fun setBoard(position: Int, vo: BoardVO) {list.set(position, vo); notifyDataSetChanged()} //update
    fun getBoard(position: Int): BoardVO {return list.get(position)} //select

    var handler: OnBoardItemClickHandler? = null
    fun setItemClickHandler(handler: OnBoardItemClickHandler) { this.handler = handler } //상세보기할떄 필요
    inner class BoardViewHolder(private val boardView: View) : RecyclerView.ViewHolder(boardView) {
        val tvTitle: TextView = boardView.findViewById(R.id.tvTitle)
        val tvWriter: TextView = boardView.findViewById(R.id.tvWriter)
        val tvRegdate: TextView = boardView.findViewById(R.id.tvRegdate)
        val tvHitcount: TextView = boardView.findViewById(R.id.tvHitcount)
        init {
            boardView.setOnClickListener {
                handler?.onItemClick(this, boardView, adapterPosition) }
        }
        fun setBoard(vo: BoardVO): Unit {
            tvTitle.text = vo.title
            tvWriter.text = vo.writer
            tvRegdate.text = vo.regdate.toString()
            tvHitcount.text = vo.hitcount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return BoardViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val boardVO = list.get(position)
        holder.setBoard(boardVO)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}