package com.irfan.stepcoin

data class Exercise(val name: String, val muscle: String, val how: String, val sets: String)

object Exercises {
    val data: LinkedHashMap<String, List<Exercise>> = linkedMapOf(
        "Chest" to listOf(
            Exercise("Push-ups", "Chest, triceps", "Hands shoulder-width on the floor, body in a straight line. Lower your chest until it almost touches, then push back up. Keep your core tight.", "3 sets × 12–20"),
            Exercise("Bench Press", "Chest", "Lie on a flat bench, grip the bar a little wider than shoulders. Lower it to mid-chest, then press straight up.", "4 × 8–12"),
            Exercise("Incline Dumbbell Press", "Upper chest", "On a 30–45° incline, press dumbbells from chest level straight up, then lower slowly.", "3 × 10–12"),
            Exercise("Chest Fly", "Chest", "Lie flat with dumbbells above your chest, slight elbow bend. Open arms out wide, then squeeze them back together.", "3 × 12–15"),
            Exercise("Dips", "Lower chest, triceps", "On parallel bars, lean slightly forward, lower until elbows are ~90°, then push back up.", "3 × 8–12")
        ),
        "Back" to listOf(
            Exercise("Pull-ups", "Back, biceps", "Hang from a bar, pull your chin over the bar, then lower with control.", "3 × max"),
            Exercise("Lat Pulldown", "Back", "Grip wide, pull the bar to your upper chest squeezing your shoulder blades, release slowly.", "4 × 10–12"),
            Exercise("Bent-over Row", "Back", "Hinge at the hips with a flat back, pull the bar/dumbbells to your waist, lower slowly.", "4 × 8–12"),
            Exercise("Deadlift", "Back, legs", "Flat back, grip the bar, drive through your heels and stand tall, then lower with control.", "4 × 6–8"),
            Exercise("Seated Row", "Back", "Pull the handle to your stomach, squeeze your back, extend your arms slowly.", "3 × 12")
        ),
        "Legs" to listOf(
            Exercise("Squats", "Legs, glutes", "Feet shoulder-width, chest up. Sit your hips back and down until thighs are parallel, then stand up.", "4 × 10–15"),
            Exercise("Lunges", "Legs, glutes", "Step forward and lower the back knee toward the floor, then push back up. Alternate legs.", "3 × 12 each"),
            Exercise("Leg Press", "Legs", "Push the platform away until legs are almost straight, lower until knees are ~90°.", "4 × 12"),
            Exercise("Leg Curl", "Hamstrings", "Curl the pad toward your glutes, squeeze, then lower slowly.", "3 × 12–15"),
            Exercise("Calf Raise", "Calves", "Rise onto your toes as high as possible, pause, then lower slowly.", "4 × 15–20")
        ),
        "Shoulders" to listOf(
            Exercise("Overhead Press", "Shoulders", "Press dumbbells or a bar from shoulder level to overhead, then lower with control.", "4 × 8–12"),
            Exercise("Lateral Raise", "Side delts", "Raise dumbbells out to the sides up to shoulder height, then lower slowly.", "3 × 12–15"),
            Exercise("Front Raise", "Front delts", "Raise dumbbells straight in front to shoulder height, then lower slowly.", "3 × 12"),
            Exercise("Shrugs", "Traps", "Hold weights, lift your shoulders straight up toward your ears, squeeze, then release.", "3 × 15")
        ),
        "Arms" to listOf(
            Exercise("Bicep Curl", "Biceps", "Keep elbows at your sides, curl the weights up, squeeze, then lower slowly.", "3 × 12"),
            Exercise("Hammer Curl", "Biceps, forearms", "Curl with palms facing each other, then lower slowly.", "3 × 12"),
            Exercise("Tricep Pushdown", "Triceps", "Push the cable down until arms are straight, squeeze, then return slowly.", "3 × 12–15"),
            Exercise("Tricep Dips", "Triceps", "Hands on a bench behind you, lower your body by bending the elbows, then push back up.", "3 × 10–15"),
            Exercise("Overhead Extension", "Triceps", "Hold one dumbbell overhead, lower it behind your head, then extend back up.", "3 × 12")
        ),
        "Abs" to listOf(
            Exercise("Crunches", "Abs", "Lie down with knees bent, curl your shoulders toward your knees, then lower slowly.", "3 × 20"),
            Exercise("Plank", "Core", "Forearms and toes on the floor, body in a straight line, hold tight.", "3 × 30–60 sec"),
            Exercise("Leg Raises", "Lower abs", "Lie flat, raise straight legs to 90°, then lower slowly without touching the floor.", "3 × 15"),
            Exercise("Russian Twist", "Obliques", "Sit and lean back slightly, twist your torso from side to side.", "3 × 20"),
            Exercise("Mountain Climbers", "Core, cardio", "In a push-up position, drive your knees to your chest alternately, fast.", "3 × 30 sec")
        ),
        "Cardio" to listOf(
            Exercise("Running", "Full body", "Steady jog or run. Warm up first and keep a pace you can maintain.", "20–30 min"),
            Exercise("Jumping Jacks", "Full body", "Jump your feet out while raising your arms overhead, return, and repeat quickly.", "3 × 40"),
            Exercise("Burpees", "Full body", "Squat, kick back to a plank, do a push-up, then jump up. Repeat.", "3 × 10–15"),
            Exercise("High Knees", "Legs, cardio", "Run in place driving your knees up to waist height, fast.", "3 × 30 sec"),
            Exercise("Skipping", "Full body", "Jump rope at a steady rhythm.", "3 × 1 min")
        )
    )
}
