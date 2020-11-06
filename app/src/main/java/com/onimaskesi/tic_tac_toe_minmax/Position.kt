package com.onimaskesi.tic_tac_toe_minmax

class Position(val elements : Elements) {

    var score : Int = 0
    var positionArray = CharArray(9){elements.EMPTY}

    var parent : Position? = null

    var whoesTour : Char? = null

    var childArray = ArrayList<Position>()

    fun parentOlustur(position : Position){

        this.parent = position

    }


}