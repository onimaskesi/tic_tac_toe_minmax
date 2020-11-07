package com.onimaskesi.tic_tac_toe_minmax

class Elements( _USER : Char = 'O', _COMPUTER : Char = 'X', _EMPTY : Char = ' ') {

    var EMPTY : Char = _EMPTY

    var USER : Char = _USER

    var COMPUTER : Char = _COMPUTER
        get(){
            if(USER == 'X'){
                return  'O'
            } else {
                return field
            }
        }
        set(value) {
            field = value
        }


}