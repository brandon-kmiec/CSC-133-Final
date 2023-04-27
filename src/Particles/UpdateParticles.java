package Particles;

import Data.Frame;
import logic.Control;

import java.util.Iterator;

public class UpdateParticles {
    // Fields
    private final ParticleSystem particleSystem;
    private final Control ctrl;

    // Constructor
    public UpdateParticles(Control ctrl, boolean reset, ParticleSystem particleSystem) {
        this.ctrl = ctrl;
        this.particleSystem = particleSystem;

        if (reset)
            update();
        else
            updateNoReset();
    }

    // Methods
    private void update() {
        Iterator<Frame> particleSystemParticles = particleSystem.getParticles();

        while (particleSystemParticles.hasNext()) {
            Frame particles = particleSystemParticles.next();
            ctrl.addSpriteToFrontBuffer(particles);
        }
    }

    private void updateNoReset() {
        Iterator<Frame> particleSystemParticles = particleSystem.getParticlesNoReset();

        while (particleSystemParticles.hasNext()) {
            Frame particles = particleSystemParticles.next();
            ctrl.addSpriteToFrontBuffer(particles);
        }
    }
}
