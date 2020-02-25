package by.paranoidandroid

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.replace(@IdRes containerId: Int, fragmentInstance: Fragment) {
    this.beginTransaction()
        .replace(containerId, fragmentInstance)
        .commitNow()
}

