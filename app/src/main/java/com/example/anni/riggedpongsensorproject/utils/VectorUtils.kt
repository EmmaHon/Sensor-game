package com.example.anni.riggedpongsensorproject.utils

import com.badlogic.gdx.math.Vector2


object VectorUtils {

    internal val TAG = VectorUtils::class.java.simpleName
    /**
     * Checks if the vector's X coordinate is inside the range [min,max],
     * adjusting it if needed.
     *
     *
     * Returns `true` if the value was adjusted, `false`
     * otherwise.
     */
    fun adjustByRangeX(
            vector: Vector2,
            min: Float,
            max: Float): Boolean {
        if (vector.x < min) {
            vector.x = min
            return true
        } else if (vector.x > max) {
            vector.x = max
            return true
        }
        return false
    }

    /**
     * Checks if the vector's Y coordinate is inside the range [min,max],
     * adjusting it if needed.
     *
     *
     * Returns `true` if the value was adjusted, `false`
     * otherwise.
     */
    fun adjustByRangeY(
            vector: Vector2,
            min: Float,
            max: Float): Boolean {
        if (vector.y < min) {
            vector.y = min
            return true
        } else if (vector.y > max) {
            vector.y = max
            return true
        }
        return false
    }

    /**
     * Checks if the vector's coordinates are inside the range [xMin,xMax] and
     * [yMin,yMax], adjusting them if needed.
     *
     *
     * Returns `true` if at least one of the values was adjusted,
     * `false` otherwise.
     */
    fun adjustByRange(
            vector: Vector2,
            xMin: Float,
            xMax: Float,
            yMin: Float,
            yMax: Float): Boolean {
        var modified = false
        if (adjustByRangeX(vector, xMin, xMax)) modified = true
        if (adjustByRangeY(vector, yMin, yMax)) modified = true
        return modified
    }

    /**
     * Checks if both the vector's coordinates are inside the range [min,max],
     * adjusting them if needed.
     *
     *
     * Returns `true` if at least one of the values was adjusted,
     * `false` otherwise.
     */
    fun adjustByRange(
            vector: Vector2,
            min: Float,
            max: Float): Boolean {
        return adjustByRange(vector, min, max, min, max)
    }

    /**
     * Uses the given value when the vector coordinates are less than or equal
     * to the specified radius value.
     */
    fun adjustDeadzone(
            vector: Vector2,
            radius: Float,
            adjustedValue: Float): Boolean {
        if (vector.len() <= radius) {
            vector.x = adjustedValue
            vector.y = adjustedValue
            return true
        }
        return false
    }
}
