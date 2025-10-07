package com.artillexstudios.axsellwands.utils;

import com.artillexstudios.axapi.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.exception.*;
import revxrsal.commands.exception.*;
import revxrsal.commands.node.ParameterNode;

import static com.artillexstudios.axsellwands.AxSellwands.CONFIG;
import static com.artillexstudios.axsellwands.AxSellwands.LANG;

public class CommandMessages extends BukkitExceptionHandler {

    @HandleException
    public void onInvalidPlayer(InvalidPlayerException e, BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-player")
                                .replace("%player%", e.input())
        ));
    }

    @HandleException
    public void onInvalidWorld(InvalidWorldException e, BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-value")
                                .replace("%value%", e.input())
        ));
    }

    @HandleException
    public void onInvalidWorld(MissingLocationParameterException e, BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-value")
                                .replace("%value%", e.input())
        ));
    }

    @HandleException
    public void onSenderNotConsole(SenderNotConsoleException e, BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.console-only")
        ));
    }

    @HandleException
    public void onSenderNotPlayer(SenderNotPlayerException e, BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.player-only")
        ));
    }

    @HandleException
    public void onMalformedEntitySelector(MalformedEntitySelectorException e, BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-selector")
        ));
    }

    @HandleException
    public void onNonPlayerEntities(NonPlayerEntitiesException e, BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-selector")
        ));
    }

    @HandleException
    public void onMoreThanOneEntity(MoreThanOneEntityException e, BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-selector")
        ));
    }

    @HandleException
    public void onEmptyEntitySelector(EmptyEntitySelectorException e, BukkitCommandActor actor) {
        try {
            throw new RuntimeException();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        actor.error(StringUtils.formatToString(
//                CONFIG.getString("prefix", "") +
//                        LANG.getString("commands.invalid-selector")
//        ));
    }

    @HandleException
    public void onEnumNotFound(@NotNull EnumNotFoundException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-value")
                                .replace("%value%", e.input())
        ));
    }

    @HandleException
    public void onExpectedLiteral(@NotNull ExpectedLiteralException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-command")
        ));
    }

    @HandleException
    public void onInputParse(@NotNull InputParseException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-command")
        ));
    }

    @HandleException
    public void onInvalidListSize(@NotNull InvalidListSizeException e, @NotNull BukkitCommandActor actor, @NotNull ParameterNode<BukkitCommandActor, ?> parameter) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-command")
        ));
    }

    @HandleException
    public void onInvalidStringSize(@NotNull InvalidStringSizeException e, @NotNull BukkitCommandActor actor, @NotNull ParameterNode<BukkitCommandActor, ?> parameter) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-command")
        ));
    }

    @HandleException
    public void onInvalidBoolean(@NotNull InvalidBooleanException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-value")
                                .replace("%value%", e.input())
        ));
    }

    @HandleException
    public void onInvalidDecimal(@NotNull InvalidDecimalException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-value")
                                .replace("%value%", e.input())
        ));
    }

    @HandleException
    public void onInvalidInteger(@NotNull InvalidIntegerException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-value")
                                .replace("%value%", e.input())
        ));
    }

    @HandleException
    public void onInvalidUUID(@NotNull InvalidUUIDException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-value")
                                .replace("%value%", e.input())
        ));
    }

    @HandleException
    public void onMissingArgument(@NotNull MissingArgumentException e, @NotNull BukkitCommandActor actor, @NotNull ParameterNode<BukkitCommandActor, ?> parameter) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.missing-argument").replace("%value%", parameter.name())
        ));
        try {
            throw new RuntimeException();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @HandleException
    public void onNoPermission(@NotNull NoPermissionException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.no-permission")
        ));
    }

    @HandleException
    public void onNumberNotInRange(@NotNull NumberNotInRangeException e, @NotNull BukkitCommandActor actor, @NotNull ParameterNode<BukkitCommandActor, Number> parameter) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.out-of-range")
                                .replace("%number%", fmt(e.input()))
                                .replace("%min%", fmt(e.minimum()))
                                .replace("%max%", fmt(e.maximum()))
        ));
    }

    @HandleException
    public void onInvalidHelpPage(@NotNull InvalidHelpPageException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-command")
        ));
    }

    @HandleException
    public void onUnknownCommand(@NotNull UnknownCommandException e, @NotNull BukkitCommandActor actor) {
        actor.error(StringUtils.formatToString(
                CONFIG.getString("prefix", "") +
                        LANG.getString("commands.invalid-command")
        ));
    }
}
