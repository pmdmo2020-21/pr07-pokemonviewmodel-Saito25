package es.iessaladillo.pedrojoya.intents.data.local.model

import android.os.Parcel
import android.os.Parcelable

// TODO: Define las propiedades de un pokemon
data class Pokemon(
    val id: Long,
    val drawable: Int,
    val name: Int,
    val attack: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeInt(drawable)
        parcel.writeInt(name)
        parcel.writeInt(attack)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pokemon> {
        override fun createFromParcel(parcel: Parcel): Pokemon {
            return Pokemon(parcel)
        }

        override fun newArray(size: Int): Array<Pokemon?> {
            return arrayOfNulls(size)
        }
    }
}