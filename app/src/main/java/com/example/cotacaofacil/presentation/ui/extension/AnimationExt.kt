package com.example.cotacaofacil.presentation.ui.extension

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation

fun View.animateCloseEffect(duration: Long = 500L) {
    // Cria uma animação de desaparecimento gradual (alpha)
    val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
    alphaAnimation.duration = duration

    // Cria uma animação de escala para reduzir o tamanho da View
    val scaleAnimation = ScaleAnimation(
        1.0f, 0.0f,  // De 100% para 0% no eixo X
        1.0f, 0.0f,  // De 100% para 0% no eixo Y
        Animation.RELATIVE_TO_SELF, 0.5f,  // Ponto central de escala no eixo X
        Animation.RELATIVE_TO_SELF, 0.5f   // Ponto central de escala no eixo Y
    )
    scaleAnimation.duration = duration

    // Define um AnimationSet para combinar várias animações
    val animationSet = AnimationSet(true)
    animationSet.addAnimation(alphaAnimation)
    animationSet.addAnimation(scaleAnimation)

    // Define o listener para a animação de finalização
    animationSet.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            // Chamado quando a animação começa
        }

        override fun onAnimationEnd(animation: Animation) {
            // Chamado quando a animação termina
            visibility = View.GONE // Oculta a View após a animação
        }

        override fun onAnimationRepeat(animation: Animation) {
            // Chamado quando a animação se repete (não utilizado neste exemplo)
        }
    })

    // Inicia a animação
    startAnimation(animationSet)
}

fun View.animateOpenEffect(duration: Long = 500L) {
    // Cria uma animação de aparecimento gradual (alpha)
    val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
    alphaAnimation.duration = duration

    // Cria uma animação de escala para aumentar o tamanho da View
    val scaleAnimation = ScaleAnimation(
        0.0f, 1.0f,  // De 0% para 100% no eixo X
        0.0f, 1.0f,  // De 0% para 100% no eixo Y
        Animation.RELATIVE_TO_SELF, 0.5f,  // Ponto central de escala no eixo X
        Animation.RELATIVE_TO_SELF, 0.5f   // Ponto central de escala no eixo Y
    )
    scaleAnimation.duration = duration

    // Define um AnimationSet para combinar várias animações
    val animationSet = AnimationSet(true)
    animationSet.addAnimation(alphaAnimation)
    animationSet.addAnimation(scaleAnimation)

    // Define o listener para a animação de finalização
    animationSet.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            visibility = View.VISIBLE // Torna a View visível antes da animação começar
        }

        override fun onAnimationEnd(animation: Animation) {
            // Chamado quando a animação termina
        }

        override fun onAnimationRepeat(animation: Animation) {
            // Chamado quando a animação se repete (não utilizado neste exemplo)
        }
    })

    // Inicia a animação
    startAnimation(animationSet)
}
