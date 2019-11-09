package com.mo.stratego.model.component

import com.badlogic.ashley.core.Component
import com.mo.stratego.model.HighlightType


/**
 * This component serves no other purpose than as flag for filtering.
 */
class HighlightComponent(val type: HighlightType) : Component