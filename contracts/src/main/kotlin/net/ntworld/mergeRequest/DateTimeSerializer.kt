@file:UseSerializers(DateTimeSerializer::class)

package net.ntworld.mergeRequest

import kotlinx.serialization.*
import org.joda.time.DateTime

/**
 * TODO: Cannot use until foundation support @SerializerFor(...)
 */
@Serializer(forClass = DateTime::class)
object DateTimeSerializer : KSerializer<DateTime> {

    override val descriptor: SerialDescriptor = PolymorphicClassDescriptor

    override fun deserialize(decoder: Decoder): DateTime {
        return DateTime(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, obj: DateTime) {
        encoder.encodeString(obj.toString())
    }

}


