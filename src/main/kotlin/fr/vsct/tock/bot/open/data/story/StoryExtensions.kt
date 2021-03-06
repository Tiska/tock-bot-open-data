/*
 *  This file is part of the tock-bot-open-data distribution.
 *  (https://github.com/voyages-sncf-technologies/tock-bot-open-data)
 *  Copyright (c) 2017 VSCT.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.vsct.tock.bot.open.data.story

import fr.vsct.tock.bot.engine.BotBus
import fr.vsct.tock.bot.engine.dialog.ContextValue
import fr.vsct.tock.bot.open.data.client.sncf.SncfOpenDataClient
import fr.vsct.tock.bot.open.data.client.sncf.SncfOpenDataClient.findPlaceValue
import fr.vsct.tock.bot.open.data.client.sncf.model.Place
import fr.vsct.tock.bot.open.data.departureDateEntity
import fr.vsct.tock.bot.open.data.destinationEntity
import fr.vsct.tock.bot.open.data.entity.PlaceValue
import fr.vsct.tock.bot.open.data.locationEntity
import fr.vsct.tock.bot.open.data.originEntity
import fr.vsct.tock.nlp.api.client.model.Entity
import fr.vsct.tock.nlp.entity.date.DateEntityRange
import fr.vsct.tock.shared.defaultZoneId
import java.time.LocalDateTime

/**
 * entity values
 */
var BotBus.origin: Place?
    get() = place(originEntity)
    set(value) = setPlace(originEntity, value)

var BotBus.location: Place?
    get() = place(locationEntity)
    set(value) = setPlace(locationEntity, value)

var BotBus.destination: Place?
    get() = place(destinationEntity)
    set(value) = setPlace(destinationEntity, value)


val BotBus.departureDate: LocalDateTime?
    get() = entityValue<DateEntityRange>(departureDateEntity)?.start()?.withZoneSameInstant(defaultZoneId)?.toLocalDateTime()

fun BotBus.returnsAndRemoveLocation(): Place? {
    return location.apply {
        removeEntityValue(locationEntity)
    }
}

fun findPlace(name: String): Place? {
    return findPlaceValue(name)?.place
}

private fun BotBus.place(entity: Entity): Place? = entityValue(entity, ::placeValue)?.place

private fun BotBus.setPlace(entity: Entity, place: Place?) = changeEntityValue(entity, place?.let { PlaceValue(place) })

private fun placeValue(context: ContextValue): PlaceValue? {
    return with(context) {
        if (evaluated && value is PlaceValue) {
            value as PlaceValue
        } else {
            null
        }
    }
}




