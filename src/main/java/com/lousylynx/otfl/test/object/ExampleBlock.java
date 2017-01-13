package com.lousylynx.otfl.test.object;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ExampleBlock extends Block {
    public ExampleBlock() {
        super(Material.ROCK);
        setRegistryName("otfl:exampleBlock");
    }
}
