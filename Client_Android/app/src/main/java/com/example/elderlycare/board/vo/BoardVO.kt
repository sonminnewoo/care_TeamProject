package com.example.elderlycare.board.vo

import User
import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class BoardVO(
    var num: Long, var title: String, var writer: String, var content: String, var regdate: Date,
    var hitcount: Long, var replycnt: Long, var user: User? //주 생성자
) : Parcelable {
    constructor(
        title: String, writer: String, content: String, regdate: Date,
        hitcount: Long, replycnt: Long, user: User?
    ) : this(0, title, writer, content, regdate, hitcount, replycnt, null)

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString()!!, // Nullable이 아닌 필드이므로 !! 연산자를 사용하여 null이 아님을 단언합니다.
        source.readString()!!,
        source.readString()!!,
        Date(source.readLong()),
        source.readLong(),
        source.readLong(),
        source.readParcelable(User::class.java.classLoader)!! // Parcelable 객체를 읽어옵니다.
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(num)
        dest.writeString(title)
        dest.writeString(writer)
        dest.writeString(content)
        dest.writeLong(regdate.time)
        dest.writeLong(hitcount)
        dest.writeLong(replycnt)
        dest.writeParcelable(user, flags) // Parcelable 객체를 쓰기 위해 writeParcelable 메서드를 사용합니다.
    }

    companion object CREATOR : Parcelable.Creator<BoardVO> {
        override fun createFromParcel(source: Parcel): BoardVO {
            return BoardVO(source)
        }

        override fun newArray(size: Int): Array<BoardVO?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "$num/$writer/$title/$content/$regdate/$hitcount/$replycnt"
    }
}
