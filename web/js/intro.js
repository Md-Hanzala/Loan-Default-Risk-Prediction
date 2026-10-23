/**
 * LoanLens Introduction Page Script
 * Lightweight, self-contained interactivity:
 * - Subtle hero cursor spotlight on fine-pointer devices
 * - Accessible scroll-triggered reveal observer
 * - Complete prefers-reduced-motion compliance
 * Zero external dependencies.
 */
(function () {
    "use strict";

    document.addEventListener("DOMContentLoaded", function () {
        initHeroSpotlight();
        initScrollReveals();
        initSmoothAnchors();
    });

    /**
     * Bounded hero spotlight tracking
     * Restrained to the hero container on non-touch devices.
     */
    function initHeroSpotlight() {
        if (!window.matchMedia("(pointer: fine)").matches) {
            return;
        }

        var hero = document.getElementById("llHero");
        var spotlight = document.getElementById("llSpotlight");
        if (!hero || !spotlight) {
            return;
        }

        var ticking = false;

        function updatePosition(x, y) {
            var rect = hero.getBoundingClientRect();
            var relativeX = Math.round(x - rect.left);
            var relativeY = Math.round(y - rect.top);
            hero.style.setProperty("--ll-spot-x", relativeX + "px");
            hero.style.setProperty("--ll-spot-y", relativeY + "px");
            spotlight.style.opacity = "1";
            ticking = false;
        }

        hero.addEventListener("mousemove", function (e) {
            if (!ticking) {
                var clientX = e.clientX;
                var clientY = e.clientY;
                window.requestAnimationFrame(function () {
                    updatePosition(clientX, clientY);
                });
                ticking = true;
            }
        });

        hero.addEventListener("mouseleave", function () {
            spotlight.style.opacity = "0";
            hero.style.removeProperty("--ll-spot-x");
            hero.style.removeProperty("--ll-spot-y");
        });
    }

    /**
     * Progressive enhancement scroll reveal
     * Reveals cards once as they enter the viewport.
     * Content remains completely visible if JS is disabled.
     */
    function initScrollReveals() {
        if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
            return;
        }

        var elements = document.querySelectorAll(".ll-reveal");
        if (!elements.length || !("IntersectionObserver" in window)) {
            return;
        }

        document.documentElement.classList.add("ll-reveals-ready");

        var observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add("ll-revealed");
                    observer.unobserve(entry.target);
                }
            });
        }, {
            threshold: 0.12,
            rootMargin: "0px 0px -40px 0px"
        });

        elements.forEach(function (el) {
            observer.observe(el);
        });
    }

    /**
     * Accessible in-page smooth scrolling for workflow explore link
     */
    function initSmoothAnchors() {
        var links = document.querySelectorAll('a[href^="#"]');
        links.forEach(function (link) {
            link.addEventListener("click", function (e) {
                var targetId = link.getAttribute("href");
                if (targetId && targetId !== "#") {
                    var target = document.querySelector(targetId);
                    if (target) {
                        e.preventDefault();
                        target.scrollIntoView({
                            behavior: window.matchMedia("(prefers-reduced-motion: reduce)").matches ? "auto" : "smooth",
                            block: "start"
                        });
                        target.focus({ preventScroll: true });
                    }
                }
            });
        });
    }
})();
