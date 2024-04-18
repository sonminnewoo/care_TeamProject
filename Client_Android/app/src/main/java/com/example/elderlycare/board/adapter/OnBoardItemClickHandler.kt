package com.example.elderlycare.board.adapter

import android.view.View

interface OnBoardItemClickHandler {
    fun onItemClick(holder: BoardAdapter.BoardViewHolder, v: View, idx: Int): Unit {

    }
}
