package com.jonesandjay123.travelphrasebook

object PhrasePresets {
    /**
     * A practical first-launch pack for Jones's Japan trip.
     * Keep these offline and editable; TTS can read the Japanese text directly.
     */
    val japanSurvivalPack: List<Sentence> = listOf(
        Sentence(chineseText = "不好意思，請問你會說英文或中文嗎？", englishText = "Excuse me, do you speak English or Chinese?", japaneseText = "すみません、英語か中国語は話せますか？"),
        Sentence(chineseText = "不好意思，我不太會說日文。", englishText = "Sorry, I don't speak Japanese very well.", japaneseText = "すみません、日本語があまり話せません。"),
        Sentence(chineseText = "可以請你說慢一點嗎？", englishText = "Could you speak more slowly?", japaneseText = "もう少しゆっくり話していただけますか？"),
        Sentence(chineseText = "可以請你幫我寫下來嗎？", englishText = "Could you write it down for me?", japaneseText = "書いていただけますか？"),
        Sentence(chineseText = "我想去這裡。", englishText = "I want to go here.", japaneseText = "ここに行きたいです。"),
        Sentence(chineseText = "請問這裡怎麼走？", englishText = "How do I get here?", japaneseText = "ここへはどう行けばいいですか？"),
        Sentence(chineseText = "請問最近的車站在哪裡？", englishText = "Where is the nearest station?", japaneseText = "一番近い駅はどこですか？"),
        Sentence(chineseText = "請問這班車會到東京車站嗎？", englishText = "Does this train go to Tokyo Station?", japaneseText = "この電車は東京駅に行きますか？"),
        Sentence(chineseText = "請問我要在哪一站下車？", englishText = "Which station should I get off at?", japaneseText = "どの駅で降りればいいですか？"),
        Sentence(chineseText = "請問這個月台對嗎？", englishText = "Is this the correct platform?", japaneseText = "このホームで合っていますか？"),
        Sentence(chineseText = "請問可以用 Suica / PASMO 嗎？", englishText = "Can I use Suica / PASMO?", japaneseText = "Suica／PASMOは使えますか？"),
        Sentence(chineseText = "請問可以刷信用卡嗎？", englishText = "Can I pay by credit card?", japaneseText = "クレジットカードは使えますか？"),
        Sentence(chineseText = "請問可以用現金嗎？", englishText = "Can I pay in cash?", japaneseText = "現金は使えますか？"),
        Sentence(chineseText = "請問多少錢？", englishText = "How much is it?", japaneseText = "いくらですか？"),
        Sentence(chineseText = "請給我收據。", englishText = "Please give me a receipt.", japaneseText = "レシートをください。"),
        Sentence(chineseText = "請問廁所在哪裡？", englishText = "Where is the restroom?", japaneseText = "トイレはどこですか？"),
        Sentence(chineseText = "我有預約，名字是 Jones。", englishText = "I have a reservation under Jones.", japaneseText = "予約しています。名前はJonesです。"),
        Sentence(chineseText = "請問可以寄放行李嗎？", englishText = "Can I leave my luggage here?", japaneseText = "荷物を預けてもいいですか？"),
        Sentence(chineseText = "請問幾點可以入住？", englishText = "What time can I check in?", japaneseText = "チェックインは何時からですか？"),
        Sentence(chineseText = "請問幾點需要退房？", englishText = "What time is check-out?", japaneseText = "チェックアウトは何時ですか？"),
        Sentence(chineseText = "請問有空位嗎？", englishText = "Do you have seats available?", japaneseText = "空いている席はありますか？"),
        Sentence(chineseText = "兩位，謝謝。", englishText = "Two people, please.", japaneseText = "二人です。お願いします。"),
        Sentence(chineseText = "一位，謝謝。", englishText = "One person, please.", japaneseText = "一人です。お願いします。"),
        Sentence(chineseText = "請問有英文或中文菜單嗎？", englishText = "Do you have an English or Chinese menu?", japaneseText = "英語か中国語のメニューはありますか？"),
        Sentence(chineseText = "請問推薦哪一道？", englishText = "What do you recommend?", japaneseText = "おすすめは何ですか？"),
        Sentence(chineseText = "我不吃牛肉。", englishText = "I don't eat beef.", japaneseText = "牛肉は食べられません。"),
        Sentence(chineseText = "我對海鮮過敏。", englishText = "I'm allergic to seafood.", japaneseText = "シーフードアレルギーがあります。"),
        Sentence(chineseText = "請不要放蔥。", englishText = "Please don't add green onions.", japaneseText = "ネギを入れないでください。"),
        Sentence(chineseText = "可以請你給我水嗎？", englishText = "Could I have some water?", japaneseText = "お水をいただけますか？"),
        Sentence(chineseText = "我要這個，謝謝。", englishText = "I'll have this, please.", japaneseText = "これをお願いします。"),
        Sentence(chineseText = "可以外帶嗎？", englishText = "Can I get this to go?", japaneseText = "持ち帰りできますか？"),
        Sentence(chineseText = "請問可以結帳嗎？", englishText = "Could I have the bill?", japaneseText = "お会計をお願いします。"),
        Sentence(chineseText = "請問免稅櫃台在哪裡？", englishText = "Where is the tax-free counter?", japaneseText = "免税カウンターはどこですか？"),
        Sentence(chineseText = "我想要這個。", englishText = "I'd like this one.", japaneseText = "これが欲しいです。"),
        Sentence(chineseText = "請問有其他尺寸嗎？", englishText = "Do you have another size?", japaneseText = "他のサイズはありますか？"),
        Sentence(chineseText = "請問可以試穿嗎？", englishText = "Can I try this on?", japaneseText = "試着してもいいですか？"),
        Sentence(chineseText = "我迷路了，可以幫我嗎？", englishText = "I'm lost. Could you help me?", japaneseText = "道に迷いました。助けていただけますか？"),
        Sentence(chineseText = "我需要幫忙。", englishText = "I need help.", japaneseText = "助けが必要です。"),
        Sentence(chineseText = "請幫我叫救護車。", englishText = "Please call an ambulance.", japaneseText = "救急車を呼んでください。"),
        Sentence(chineseText = "請幫我叫警察。", englishText = "Please call the police.", japaneseText = "警察を呼んでください。"),
        Sentence(chineseText = "我的護照不見了。", englishText = "My passport is missing.", japaneseText = "パスポートをなくしました。"),
        Sentence(chineseText = "我的手機不見了。", englishText = "My phone is missing.", japaneseText = "携帯電話をなくしました。"),
        Sentence(chineseText = "謝謝你的幫忙。", englishText = "Thank you for your help.", japaneseText = "助けてくれてありがとうございます。")
    )
}
