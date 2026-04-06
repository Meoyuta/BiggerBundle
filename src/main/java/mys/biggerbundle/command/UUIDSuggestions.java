package mys.biggerbundle.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import mys.biggerbundle.data.StorageBagSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class UUIDSuggestions implements SuggestionProvider<CommandSourceStack> {
    public static final UUIDSuggestions INSTANCE = new UUIDSuggestions();

    private UUIDSuggestions() {
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        var savedData = StorageBagSavedData.get(context.getSource().getServer());
        var uuidSet = savedData.getUUIDSet();
        var uuidList = new ArrayList<String>();
        for (var uuid : uuidSet) {
            uuidList.add(uuid.toString());
        }
        return SharedSuggestionProvider.suggest(uuidList, builder);
    }
}
