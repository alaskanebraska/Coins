package com.example.pavel.coins.Model

import android.arch.persistence.room.Embedded

class Quote(@Embedded val USD:USD) {
}