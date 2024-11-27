package com.android.infosystest

sealed class OnClick {
    object Retry: OnClick()
    data class Insert(val todo: Todo): OnClick()
}
