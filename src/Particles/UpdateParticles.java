package Particles;

import Data.Frame;
import logic.Control;

import java.util.Iterator;

public class UpdateParticles {
    // Fields
    private final ParticleSystem particleSystem;
    private final Control ctrl;

    // Constructor
    public UpdateParticles(Control ctrl, ParticleSystem particleSystem) {
        this.ctrl = ctrl;
        this.particleSystem = particleSystem;
        update();
    }

    // Methods
    private void update(){
        Iterator<Frame> particleSystemParticles = particleSystem.getParticles();

        while (particleSystemParticles.hasNext()){
            Frame particles = particleSystemParticles.next();
            ctrl.addSpriteToFrontBuffer(particles);
        }
    }
}
