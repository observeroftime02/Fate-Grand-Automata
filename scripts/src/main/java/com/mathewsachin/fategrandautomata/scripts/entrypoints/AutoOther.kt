package com.mathewsachin.fategrandautomata.scripts.entrypoints

import com.mathewsachin.fategrandautomata.scripts.IFgoAutomataApi
import com.mathewsachin.fategrandautomata.scripts.modules.Game
import com.mathewsachin.libautomata.EntryPoint
import com.mathewsachin.libautomata.ExitManager
import com.mathewsachin.libautomata.Region
import com.mathewsachin.libautomata.ScriptExitException
import javax.inject.Inject
import javax.inject.Provider
import kotlin.time.seconds

class AutoOther @Inject constructor(
    val fp: Provider<AutoFriendGacha>,
    val lottery: Provider<AutoLottery>,
    val giftBox: Provider<AutoGiftBox>,
    val supportImageMaker: Provider<SupportImageMaker>,
    exitManager: ExitManager,
    fgAutomataApi: IFgoAutomataApi
) : EntryPoint(exitManager), IFgoAutomataApi by fgAutomataApi {
    override fun script(): Nothing {
        val lotteryCheckRegion = Region(150, 800, 340, 230)

        1.seconds.wait()

        val entryPoint = when {
            images.friendSummon in game.fpSummonCheck || images.fpSummonContinue in game.fpContinueSummonRegion ->
                fp.get()
            images.finishedLotteryBox in lotteryCheckRegion || images.finishedLotteryBox in Game.finishedLotteryBoxRegion ->
                lottery.get()
            images.goldXP in game.scriptArea || images.silverXP in game.scriptArea ->
                giftBox.get()
            images.supportRegionTool in game.supportRegionToolSearchRegion ->
                supportImageMaker.get()
            else -> throw ScriptExitException(messages.cannotDetectScriptType)
        }

        entryPoint.script()
    }
}