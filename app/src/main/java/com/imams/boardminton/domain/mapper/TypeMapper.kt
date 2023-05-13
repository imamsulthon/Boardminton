package com.imams.boardminton.domain.mapper

import com.imams.boardminton.domain.model.IMatchType
import com.imams.boardminton.domain.model.ISide
import com.imams.boardminton.engine.data.model.MatchType
import com.imams.boardminton.engine.data.model.Side

fun MatchType.toVp(): IMatchType = when (this) {
    MatchType.Single -> IMatchType.Single
    MatchType.Double -> IMatchType.Double
}

fun IMatchType.toModel(): MatchType = when (this) {
    IMatchType.Single -> MatchType.Single
    IMatchType.Double -> MatchType.Double
}

fun IMatchType.isSingle() = this == IMatchType.Single

fun ISide.toVp(): Side = when (this) {
    ISide.A -> Side.A
    ISide.B -> Side.B
}

fun Side.toModel(): ISide = when (this) {
    Side.A -> ISide.A
    Side.B -> ISide.B
}
