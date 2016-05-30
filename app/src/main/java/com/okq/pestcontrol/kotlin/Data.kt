package com.okq.pestcontrol.kotlin

import java.util.*

/**
 * Created by zst on 2016/5/26 0026.
 * 描述:
 */
object Data {

    fun getEnvirsName(environments: MutableList<String>): Array<String> {
        val envirNames = ArrayList<String>()
        envirList.forEach { envirNames.add(it.name) }
        environments.forEach { envirNames.remove(it.split("=")[0]) }
        return envirNames.toTypedArray();
    }
    fun getPestsName(pests: MutableList<String>): Array<String> {
        val pestNames = ArrayList<String>()
        pestList.forEach { pestNames.add(it.name) }
        pests.forEach { pestNames.remove(it.split("=")[0]) }
        return pestNames.toTypedArray();
    }

    val envirList = arrayListOf(
            Envir("硬度", "环境0"),
            Envir("温度", "环境1"),
            Envir("湿度", "环境2"),
            Envir("露点", "环境3"),
            Envir("光照度", "环境4"),
            Envir("蒸发量", "环境5"),
            Envir("风向", "环境6"),
            Envir("风速", "环境7"),
            Envir("光合辐射", "环境8"),
            Envir("雨量", "环境10"),
            Envir("土壤温度", "环境11"),
            Envir("土壤水分", "环境12"),
            Envir("大气压", "环境13"),
            Envir("二氧化碳", "环境14"),
            Envir("土壤盐分", "环境15"),
            Envir("PH", "环境49"),
            Envir("PM2.5", "环境9"),
            Envir("总辐射", "环境21"),
            Envir("PH(土壤)", "环境20"),
            Envir("氨气", "环境16")
    )

    val pestList = arrayListOf(
            Pest("亚洲玉米螟", "害虫1"),
            Pest("草地螟", "害虫2"),
            Pest("茶尺蠖", "害虫3"),
            Pest("大螟", "害虫4"),
            Pest("稻纵卷叶螟", "害虫5"),
            Pest("豆野螟", "害虫6"),
            Pest("二化螟", "害虫7"),
            Pest("甘蔗螟虫", "害虫8"),
            Pest("瓜实蝇", "害虫9"),
            Pest("国槐小卷蛾", "害虫10"),
            Pest("金纹细蛾", "害虫11"),
            Pest("梨小食心虫", "害虫12"),
            Pest("落叶松毛虫", "害虫13"),
            Pest("马尾松毛虫", "害虫14"),
            Pest("美国白蛾", "害虫15"),
            Pest("棉铃虫", "害虫16"),
            Pest("棉盲蝽", "害虫17"),
            Pest("苹果毒蛾", "害虫18"),
            Pest("苹果蠹蛾", "害虫19"),
            Pest("桃小实心虫", "害虫20"),
            Pest("甜菜夜蛾", "害虫21"),
            Pest("舞毒蛾", "害虫22"),
            Pest("小菜蛾", "害虫23"),
            Pest("小地老虎", "害虫24"),
            Pest("斜纹夜蛾", "害虫25"),
            Pest("烟青虫", "害虫26"),
            Pest("油松毛虫", "害虫27"),
            Pest("玉米螟", "害虫28"),
            Pest("粘虫", "害虫29")
    )

    data class Envir(val name: String, val index: String)
    data class Pest(val name: String, val index: String)
}