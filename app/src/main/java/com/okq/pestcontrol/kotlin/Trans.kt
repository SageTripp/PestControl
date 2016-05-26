package com.okq.pestcontrol.kotlin

/**
 * Created by zst on 2016/5/26 0026.
 * 描述:
 */
class Trans {
    companion object {
        fun transEnvir(envirs: String, direction: TransDirection = TransDirection.INDEX2NAME): String {
            val split = envirs.split(",")
            val sb = StringBuilder()
            split.forEachIndexed { index, string ->
                val sp = string.split("=")
                val envir = Data.envirList.find { it.index.equals(sp[0]) or it.name.equals(sp[0]) }
                if (null != envir) {
                    when (direction) {
                        TransDirection.INDEX2NAME -> {
                            sb.append(string.replace(envir.index, envir.name))
                        }
                        TransDirection.NAME2INDEX -> {
                            sb.append(string.replace(envir.name, envir.index))
                        }
                    }
                    sb.append(",")
                }
            }
            if (sb.endsWith(","))
                sb.deleteCharAt(sb.length - 1)
            return sb.toString()
        }

        fun transPest(pests: String, direction: TransDirection = TransDirection.INDEX2NAME): String {
            val split = pests.split(",")
            val sb = StringBuilder()
            split.forEachIndexed { index, string ->
                val sp = string.split("=")
                val pest = Data.pestList.find { it.index.equals(sp[0]) or it.name.equals(sp[0]) }
                if (null != pest) {
                    when (direction) {
                        TransDirection.INDEX2NAME -> {
                            sb.append(string.replace(pest.index, pest.name))
                        }
                        TransDirection.NAME2INDEX -> {
                            sb.append(string.replace(pest.name, pest.index))
                        }
                    }
                    sb.append(",")
                }
            }
            if (sb.endsWith(","))
                sb.deleteCharAt(sb.length - 1)
            return sb.toString()
        }
    }

    enum class TransDirection {
        INDEX2NAME,
        NAME2INDEX
    }
}