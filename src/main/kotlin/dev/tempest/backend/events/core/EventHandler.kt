package dev.tempest.backend.events.core

import dev.tempest.backend.events.core.imp.Event
import dev.tempest.backend.events.core.imp.Priority
import java.util.concurrent.CopyOnWriteArrayList
import java.lang.IllegalAccessException
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.HashMap

/**
 * @author DarkMagician6
 * @since 02-02-2014
 * Translated to kotlin by TechAle
 */
object EventHandler {
    private val REGISTRY_MAP = HashMap<Class<out Event>, MutableList<MethodData>>()
    fun register(`object`: Any) {
        for (method in `object`.javaClass.declaredMethods) {
            if (!isMethodBad(method)) {
                register(method, `object`)
            }
        }
    }

    fun register(`object`: Any, eventClass: Class<out Event>) {
        for (method in `object`.javaClass.declaredMethods) {
            if (!isMethodBad(method, eventClass)) {
                register(method, `object`)
            }
        }
    }

    fun unregister(`object`: Any) {
        for (dataList in REGISTRY_MAP.values) {
            dataList.removeIf { data: MethodData -> data.source == `object` }
        }
        cleanMap(true)
    }

    fun unregister(`object`: Any, eventClass: Class<out Event>) {
        if (REGISTRY_MAP.containsKey(eventClass)) {
            REGISTRY_MAP[eventClass]!!.removeIf { data: MethodData -> data.source == `object` }
            cleanMap(true)
        }
    }

    private fun register(method: Method, `object`: Any) {
        val indexClass = method.parameterTypes[0] as Class<out Event>
        val data = MethodData(`object`, method, method.getAnnotation(EventTarget::class.java).value)
        if (!data.target.isAccessible) {
            data.target.isAccessible = true
        }
        if (REGISTRY_MAP.containsKey(indexClass)) {
            if (!REGISTRY_MAP[indexClass]!!.contains(data)) {
                REGISTRY_MAP[indexClass]!!.add(data)
                sortListValue(indexClass)
            }
        } else {
            REGISTRY_MAP.put(indexClass, object : CopyOnWriteArrayList<MethodData>() {
                private val serialVersionUID = 666L

                init {
                    add(data)
                }
            })
        }
    }

    fun removeEntry(indexClass: Class<out Event?>) {
        val mapIterator: MutableIterator<Map.Entry<Class<out Event>, List<MethodData>>> = REGISTRY_MAP.entries.iterator()
        while (mapIterator.hasNext()) {
            if (mapIterator.next().key == indexClass) {
                mapIterator.remove()
                break
            }
        }
    }

    fun cleanMap(onlyEmptyEntries: Boolean) {
        val mapIterator: MutableIterator<Map.Entry<Class<out Event>, List<MethodData>>> = REGISTRY_MAP.entries.iterator()
        while (mapIterator.hasNext()) {
            if (!onlyEmptyEntries || mapIterator.next().value.isEmpty()) {
                mapIterator.remove()
            }
        }
    }

    private fun sortListValue(indexClass: Class<out Event>) {
        val sortedList: MutableList<MethodData> = CopyOnWriteArrayList()
        for (priority in Priority.VALUE_ARRAY) {
            for (data in REGISTRY_MAP[indexClass]!!) {
                if (data.priority == priority) {
                    sortedList.add(data)
                }
            }
        }
        REGISTRY_MAP[indexClass] = sortedList
    }

    private fun isMethodBad(method: Method): Boolean {
        return method.parameterTypes.size != 1 || !method.isAnnotationPresent(EventTarget::class.java)
    }

    private fun isMethodBad(method: Method, eventClass: Class<out Event>): Boolean {
        return isMethodBad(method) || method.parameterTypes[0] != eventClass
    }

    fun call(event: Event): Event {
        val dataList: List<MethodData>? = REGISTRY_MAP[event.javaClass]
        if (dataList != null) {
            for (data in dataList) {
                invoke(data, event)
            }
        }
        return event
    }

    private operator fun invoke(data: MethodData, argument: Event) {
        try {
            data.target.invoke(data.source, argument)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    private class MethodData(val source: Any, val target: Method, val priority: Byte)
}