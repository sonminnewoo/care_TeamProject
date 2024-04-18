import android.os.Parcel
import android.os.Parcelable
import java.util.Date // Date 클래스를 사용합니다.

data class User(
        var userId: Long,
        var name: String,
        var email: String,
        var address: String,
        var password: String,
        var phoneNumber: String,
        var country: String,
        var image: String?,
        var createdAt: Date, // LocalDateTime 대신 Date를 사용합니다.
        var updatedAt: Date // LocalDateTime 대신 Date를 사용합니다.
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readLong(),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString(),
                Date(parcel.readLong()), // Date 타입을 사용하므로 Long 값에서 바로 Date로 변환합니다.
                Date(parcel.readLong()) // Date 타입을 사용하므로 Long 값에서 바로 Date로 변환합니다.
        )



        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeLong(userId)
                parcel.writeString(name)
                parcel.writeString(email)
                parcel.writeString(address)
                parcel.writeString(password)
                parcel.writeString(phoneNumber)
                parcel.writeString(country)
                parcel.writeString(image)
                parcel.writeLong(createdAt.time) // Date 타입을 사용하므로 Date 객체에서 Long 값으로 변환합니다.
                parcel.writeLong(updatedAt.time) // Date 타입을 사용하므로 Date 객체에서 Long 값으로 변환합니다.
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<User> {
                override fun createFromParcel(parcel: Parcel): User {
                        return User(parcel)
                }

                override fun newArray(size: Int): Array<User?> {
                        return arrayOfNulls(size)
                }
        }
}
