<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LoanLens — Loan Quality Analytics</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="ll-page-wrap">

    <!-- Ambient Technical Background Mesh -->
    <div class="ambient-bg" aria-hidden="true">
        <div class="bg-grid"></div>
        <div class="mesh-layer mesh-indigo"></div>
        <div class="mesh-layer mesh-teal"></div>
        <div class="mesh-layer mesh-ambient"></div>
    </div>

    <!-- Header Navigation Bar -->
    <header class="ll-header">
        <div class="ll-container">
            <div class="ll-header-inner">
                <a href="index.jsp" class="ll-brand" aria-label="LoanLens Home">
                    <svg width="28" height="28" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg" class="ll-brand-icon" aria-hidden="true">
                        <circle cx="16" cy="16" r="13" stroke="url(#llLogoGrad)" stroke-width="2" stroke-dasharray="3 2" />
                        <circle cx="16" cy="16" r="7" stroke="#69E4D0" stroke-width="2" />
                        <circle cx="16" cy="16" r="2.5" fill="#F0F5FF" />
                        <line x1="3" y1="16" x2="8" y2="16" stroke="#69E4D0" stroke-width="2" stroke-linecap="round" />
                        <line x1="24" y1="16" x2="29" y2="16" stroke="#8293FF" stroke-width="2" stroke-linecap="round" />
                        <defs>
                            <linearGradient id="llLogoGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                                <stop offset="0%" stop-color="#69E4D0" />
                                <stop offset="100%" stop-color="#8293FF" />
                            </linearGradient>
                        </defs>
                    </svg>
                    <div class="ll-brand-meta">
                        <div class="ll-wordmark">Loan<span class="ll-accent-text">Lens</span></div>
                        <div class="ll-brand-subtitle">LOAN QUALITY ANALYTICS</div>
                    </div>
                </a>

                <div class="ll-header-actions">
                    <span class="ll-badge-demo">
                        <span class="ll-badge-dot" aria-hidden="true"></span>
                        DEVELOPMENT DEMO
                    </span>
                    <a href="workspace.jsp" class="ll-header-nav-link">
                        Enter Workspace <span aria-hidden="true">&rarr;</span>
                    </a>
                </div>
            </div>
        </div>
    </header>

    <!-- Hero Section with Animated Lens & Introduction -->
    <main>
        <section class="ll-hero" id="llHero" aria-label="Product Introduction">
            <div class="ll-hero-spotlight" id="llSpotlight" aria-hidden="true"></div>
            <div class="ll-hero-grid-canvas" aria-hidden="true"></div>

            <div class="ll-container">
                <div class="ll-hero-content">

                    <!-- Animated Analytical Lens Visual -->
                    <div class="ll-lens-stage" aria-hidden="true">
                        <svg class="ll-lens-svg" viewBox="0 0 320 320" width="280" height="280" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <defs>
                                <linearGradient id="llLensGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                                    <stop offset="0%" stop-color="#69E4D0" stop-opacity="0.95" />
                                    <stop offset="100%" stop-color="#8293FF" stop-opacity="0.95" />
                                </linearGradient>
                                <linearGradient id="llRayGrad" x1="0%" y1="50%" x2="100%" y2="50%">
                                    <stop offset="0%" stop-color="#69E4D0" stop-opacity="0" />
                                    <stop offset="50%" stop-color="#69E4D0" stop-opacity="0.95" />
                                    <stop offset="100%" stop-color="#8293FF" stop-opacity="0" />
                                </linearGradient>
                                <radialGradient id="llGlowGrad" cx="50%" cy="50%" r="50%">
                                    <stop offset="0%" stop-color="#69E4D0" stop-opacity="0.25" />
                                    <stop offset="60%" stop-color="#8293FF" stop-opacity="0.08" />
                                    <stop offset="100%" stop-color="#080F1F" stop-opacity="0" />
                                </radialGradient>
                            </defs>

                            <!-- Ambient lens center glow -->
                            <circle cx="160" cy="160" r="110" fill="url(#llGlowGrad)" class="ll-lens-core-glow" />

                            <!-- Outer dashed calibration orbit -->
                            <circle cx="160" cy="160" r="126" stroke="#27364C" stroke-width="1.5" stroke-dasharray="4 8" class="ll-lens-outer-orbit" />

                            <!-- Reticle crosshair ticks -->
                            <line x1="160" y1="20" x2="160" y2="36" stroke="#69E4D0" stroke-width="1.5" stroke-linecap="round" class="ll-lens-tick" />
                            <line x1="160" y1="284" x2="160" y2="300" stroke="#69E4D0" stroke-width="1.5" stroke-linecap="round" class="ll-lens-tick" />
                            <line x1="20" y1="160" x2="36" y2="160" stroke="#8293FF" stroke-width="1.5" stroke-linecap="round" class="ll-lens-tick" />
                            <line x1="284" y1="160" x2="300" y2="160" stroke="#8293FF" stroke-width="1.5" stroke-linecap="round" class="ll-lens-tick" />

                            <!-- Primary optical lens ring with animated stroke-dash -->
                            <circle cx="160" cy="160" r="92" stroke="url(#llLensGrad)" stroke-width="2.5" class="ll-lens-ring-primary" stroke-linecap="round" />

                            <!-- Inner rotating analytical iris -->
                            <circle cx="160" cy="160" r="54" stroke="#8293FF" stroke-width="1.5" stroke-dasharray="6 6" class="ll-lens-ring-inner" />

                            <!-- Signal beam passing through lens -->
                            <line x1="28" y1="160" x2="292" y2="160" stroke="url(#llRayGrad)" stroke-width="2" class="ll-lens-beam" stroke-linecap="round" />

                            <!-- Connected analytical data nodes -->
                            <g class="ll-lens-nodes">
                                <circle cx="95" cy="108" r="4.5" fill="#69E4D0" class="ll-lens-node ll-node-1" />
                                <circle cx="225" cy="108" r="3.5" fill="#8293FF" class="ll-lens-node ll-node-2" />
                                <circle cx="205" cy="212" r="4" fill="#69E4D0" class="ll-lens-node ll-node-3" />
                                <circle cx="115" cy="212" r="3" fill="#8293FF" class="ll-lens-node ll-node-4" />

                                <line x1="95" y1="108" x2="160" y2="160" stroke="rgba(105, 228, 208, 0.4)" stroke-width="1" stroke-dasharray="3 3" />
                                <line x1="225" y1="108" x2="160" y2="160" stroke="rgba(130, 147, 255, 0.4)" stroke-width="1" stroke-dasharray="3 3" />
                                <line x1="205" y1="212" x2="160" y2="160" stroke="rgba(105, 228, 208, 0.4)" stroke-width="1" stroke-dasharray="3 3" />
                                <line x1="115" y1="212" x2="160" y2="160" stroke="rgba(130, 147, 255, 0.4)" stroke-width="1" stroke-dasharray="3 3" />
                            </g>

                            <!-- Central focal point & pulse ring -->
                            <circle cx="160" cy="160" r="5" fill="#F0F5FF" class="ll-lens-focal-point" />
                            <circle cx="160" cy="160" r="10" stroke="#69E4D0" stroke-width="1.2" class="ll-lens-focal-pulse" />
                        </svg>
                    </div>

                    <!-- Eyebrow Tag -->
                    <div class="ll-eyebrow">
                        <span>CREDIT ANALYTICS</span>
                        <span class="ll-eyebrow-sep">/</span>
                        <span>EVALUATION WORKFLOW</span>
                    </div>

                    <!-- Primary Headline -->
                    <h1 class="ll-headline">
                        A clearer view of <span class="ll-accent-text">loan data.</span>
                    </h1>

                    <!-- Supporting Copy -->
                    <p class="ll-subtitle">
                        Explore application features, inspect model output, and trace each evaluation through one transparent workflow.
                    </p>

                    <!-- Workflow Signal Indicator -->
                    <div class="ll-workflow-signal" aria-label="Analytical Pipeline Sequence">
                        <div class="ll-signal-step">
                            <span class="ll-signal-num">01</span>
                            <span class="ll-signal-name">INPUT</span>
                        </div>
                        <div class="ll-signal-connector" aria-hidden="true">
                            <span class="ll-signal-pulse"></span>
                        </div>
                        <div class="ll-signal-step">
                            <span class="ll-signal-num">02</span>
                            <span class="ll-signal-name">INFERENCE</span>
                        </div>
                        <div class="ll-signal-connector" aria-hidden="true">
                            <span class="ll-signal-pulse"></span>
                        </div>
                        <div class="ll-signal-step">
                            <span class="ll-signal-num">03</span>
                            <span class="ll-signal-name">RECORD</span>
                        </div>
                    </div>

                    <!-- Primary and Secondary Actions -->
                    <div class="ll-actions">
                        <a href="workspace.jsp" class="ll-enter-btn">
                            Enter Workspace <span class="ll-btn-arrow" aria-hidden="true">&rarr;</span>
                        </a>
                        <a href="#workflow-details" class="ll-explore-link">
                            Explore the workflow <span aria-hidden="true">&darr;</span>
                        </a>
                    </div>

                </div>
            </div>
        </section>

        <!-- Detailed Workflow Cards Section -->
        <section id="workflow-details" class="ll-workflow-section" tabindex="-1">
            <div class="ll-container">

                <div class="ll-section-header ll-reveal">
                    <span class="ll-section-badge">PIPELINE ARCHITECTURE</span>
                    <h2 class="ll-section-title">How the Evaluation Workflow Operates</h2>
                    <p class="ll-section-desc">
                        Every submission follows a deterministic three-stage architecture separating validation, model inference, and operational storage.
                    </p>
                </div>

                <div class="ll-workflow-grid">

                    <!-- Card 01: Input -->
                    <div class="ll-workflow-card ll-reveal">
                        <div class="ll-card-header">
                            <span class="ll-card-step-badge">01</span>
                            <div class="ll-card-icon-wrap" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                                    <polyline points="14 2 14 8 20 8"/>
                                    <line x1="16" y1="13" x2="8" y2="13"/>
                                    <line x1="16" y1="17" x2="8" y2="17"/>
                                    <polyline points="10 9 9 9 8 9"/>
                                </svg>
                            </div>
                        </div>
                        <h3 class="ll-card-title">01 — Input</h3>
                        <p class="ll-card-body">
                            Provide the nine application features required by the current model interface.
                        </p>
                    </div>

                    <!-- Card 02: Inference -->
                    <div class="ll-workflow-card ll-reveal">
                        <div class="ll-card-header">
                            <span class="ll-card-step-badge">02</span>
                            <div class="ll-card-icon-wrap" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83"/>
                                    <circle cx="12" cy="12" r="3"/>
                                </svg>
                            </div>
                        </div>
                        <h3 class="ll-card-title">02 — Inference</h3>
                        <p class="ll-card-body">
                            The configured prediction provider returns a class and associated model metadata.
                        </p>
                    </div>

                    <!-- Card 03: Record -->
                    <div class="ll-workflow-card ll-reveal">
                        <div class="ll-card-header">
                            <span class="ll-card-step-badge">03</span>
                            <div class="ll-card-icon-wrap" aria-hidden="true">
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/>
                                    <polyline points="17 21 17 13 7 13 7 21"/>
                                    <polyline points="7 3 7 8 15 8"/>
                                </svg>
                            </div>
                        </div>
                        <h3 class="ll-card-title">03 — Record</h3>
                        <p class="ll-card-body">
                            The application records the submitted features and prediction output according to the active storage configuration.
                        </p>
                    </div>

                </div>

                <!-- Demonstration Disclosure Banner -->
                <div class="ll-disclaimer-wrap ll-reveal">
                    <div class="ll-disclaimer-card" role="region" aria-label="Demonstration Notice">
                        <div class="ll-disclaimer-icon" aria-hidden="true">
                            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
                                <line x1="12" y1="9" x2="12" y2="13"/>
                                <line x1="12" y1="17" x2="12.01" y2="17"/>
                            </svg>
                        </div>
                        <div class="ll-disclaimer-text">
                            <strong>DEMONSTRATION DISCLOSURE:</strong>
                            This project is a development demonstration. The configured provider may return simulated predictions. A model label is not, by itself, a verified loan-default determination or a lending approval/rejection decision.
                        </div>
                    </div>
                </div>

            </div>
        </section>
    </main>

    <!-- Understated Product Footer -->
    <footer class="ll-footer">
        <div class="ll-container">
            <div class="ll-footer-inner">
                <div class="ll-footer-brand">
                    <span>LoanLens</span>
                    <span aria-hidden="true">&bull;</span>
                    <span>Loan Quality Analytics Demonstration</span>
                </div>
                <div>
                    <a href="workspace.jsp" class="ll-footer-link">
                        Enter Workspace <span aria-hidden="true">&rarr;</span>
                    </a>
                </div>
            </div>
        </div>
    </footer>

    <!-- Self-contained Intro Script -->
    <script src="js/intro.js"></script>
</body>
</html>
