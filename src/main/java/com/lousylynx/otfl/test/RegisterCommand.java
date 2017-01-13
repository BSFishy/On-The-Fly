package com.lousylynx.otfl.test;

import com.lousylynx.otfl.api.OtflException;
import com.lousylynx.otfl.library.OtflLibrary;
import com.lousylynx.otfl.library.register.BlockRegister;
import com.lousylynx.otfl.library.register.object.BlockObject;
import com.lousylynx.otfl.test.block.ExampleBlock;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterCommand implements ICommand {
    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "register";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>(Collections.singleton("register"));
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        try {
            OtflLibrary.getRegisteries().forEach(r -> {
                if (r instanceof BlockRegister) {
                    try {
                        r.register(new BlockObject(new ExampleBlock()));
                    } catch (OtflException e) {
                        e.printStackTrace();
                    }
                }
            });
            OtflLibrary.register();
        } catch (OtflException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings, @Nullable BlockPos blockPos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
