/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk.camel.components.filemanager.strategy;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Expression;
import org.apache.camel.component.file.GenericFileExclusiveReadLockStrategy;
import org.apache.camel.component.file.GenericFileProcessStrategy;
import org.apache.camel.component.file.strategy.GenericFileDeleteProcessStrategy;
import org.apache.camel.component.file.strategy.GenericFileExpressionRenamer;
import org.apache.camel.component.file.strategy.GenericFileRenameProcessStrategy;
import org.apache.camel.spi.Language;
import org.apache.camel.util.ObjectHelper;

import redhawk.camel.components.filemanager.RedhawkFileContainer;

public final class RedhawkFileProcessStrategyFactory {

    private RedhawkFileProcessStrategyFactory() {
    }

    public static GenericFileProcessStrategy<RedhawkFileContainer> createGenericFileProcessStrategy(CamelContext context, Map<String, Object> params) {

        // We assume a value is present only if its value not null for String and 'true' for boolean
        Expression moveExpression = (Expression) params.get("move");
        Expression moveFailedExpression = (Expression) params.get("moveFailed");
        Expression preMoveExpression = (Expression) params.get("preMove");
        boolean isNoop = params.get("noop") != null;
        boolean isDelete = params.get("delete") != null;
        boolean isMove = moveExpression != null || preMoveExpression != null || moveFailedExpression != null;

        if (isDelete) {
            GenericFileDeleteProcessStrategy<RedhawkFileContainer> strategy = new GenericFileDeleteProcessStrategy<RedhawkFileContainer>();
            strategy.setExclusiveReadLockStrategy(getExclusiveReadLockStrategy(params));
            if (preMoveExpression != null) {
                GenericFileExpressionRenamer<RedhawkFileContainer> renamer = new GenericFileExpressionRenamer<RedhawkFileContainer>();
                renamer.setExpression(preMoveExpression);
                strategy.setBeginRenamer(renamer);
            }
            if (moveFailedExpression != null) {
                GenericFileExpressionRenamer<RedhawkFileContainer> renamer = new GenericFileExpressionRenamer<RedhawkFileContainer>();
                renamer.setExpression(moveFailedExpression);
                strategy.setFailureRenamer(renamer);
            }
            return strategy;
        } else if (isMove || isNoop) {
            GenericFileRenameProcessStrategy<RedhawkFileContainer> strategy = new GenericFileRenameProcessStrategy<RedhawkFileContainer>();
            strategy.setExclusiveReadLockStrategy(getExclusiveReadLockStrategy(params));
            if (!isNoop) {
                // move on commit is only possible if not noop
                if (moveExpression != null) {
                    GenericFileExpressionRenamer<RedhawkFileContainer> renamer = new GenericFileExpressionRenamer<RedhawkFileContainer>();
                    renamer.setExpression(moveExpression);
                    strategy.setCommitRenamer(renamer);
                } else {
                    strategy.setCommitRenamer(getDefaultCommitRenamer(context));
                }
            }
            // both move and noop supports pre move
            if (preMoveExpression != null) {
                GenericFileExpressionRenamer<RedhawkFileContainer> renamer = new GenericFileExpressionRenamer<RedhawkFileContainer>();
                renamer.setExpression(preMoveExpression);
                strategy.setBeginRenamer(renamer);
            }
            // both move and noop supports move failed
            if (moveFailedExpression != null) {
                GenericFileExpressionRenamer<RedhawkFileContainer> renamer = new GenericFileExpressionRenamer<RedhawkFileContainer>();
                renamer.setExpression(moveFailedExpression);
                strategy.setFailureRenamer(renamer);
            }
            return strategy;
        } else {
            // default strategy will move files in a .camel/ subfolder where the file was consumed
            GenericFileRenameProcessStrategy<RedhawkFileContainer> strategy = new GenericFileRenameProcessStrategy<RedhawkFileContainer>();
            strategy.setExclusiveReadLockStrategy(getExclusiveReadLockStrategy(params));
            strategy.setCommitRenamer(getDefaultCommitRenamer(context));
            return strategy;
        }
    }

    /**
     * Defines the rename strategy for the GenericFileProcess Strategy your using. This is 
     * what makes the .camel directory. 
     * 
     * @param context
     * @return
     */
    private static GenericFileExpressionRenamer<RedhawkFileContainer> getDefaultCommitRenamer(CamelContext context) {
        // use context to lookup language to let it be loose coupled
        Language language = context.resolveLanguage("file");
        
        //Expression expression = language.createExpression("${file:parent}/.camel/${file:onlyname}");
        /*
         * Create a .camel directory for the file at it's current root. 
         */
        Expression expression = language.createExpression(".camel/${file:onlyname}");
        return new GenericFileExpressionRenamer<RedhawkFileContainer>(expression);
    }

    @SuppressWarnings("unchecked")
    private static GenericFileExclusiveReadLockStrategy<RedhawkFileContainer> getExclusiveReadLockStrategy(Map<String, Object> params) {
        GenericFileExclusiveReadLockStrategy<RedhawkFileContainer> strategy = (GenericFileExclusiveReadLockStrategy<RedhawkFileContainer>) params.get("exclusiveReadLockStrategy");
        if (strategy != null) {
            return strategy;
        }

        // no explicit strategy set then fallback to readLock option
        String readLock = (String) params.get("readLock");
        if (ObjectHelper.isNotEmpty(readLock)) {
            if ("none".equals(readLock) || "false".equals(readLock)) {
                return null;
            } else if ("fileLock".equals(readLock)) {
                GenericFileExclusiveReadLockStrategy<RedhawkFileContainer> readLockStrategy = new RedhawkFileLockExclusiveReadLockStrategy();
                Long timeout = (Long) params.get("readLockTimeout");
                if (timeout != null) {
                    readLockStrategy.setTimeout(timeout);
                }
                Long checkInterval = (Long) params.get("readLockCheckInterval");
                if (checkInterval != null) {
                    readLockStrategy.setCheckInterval(checkInterval);
                }
                return readLockStrategy;
            } else if ("rename".equals(readLock)) {
                GenericFileExclusiveReadLockStrategy<RedhawkFileContainer> readLockStrategy = new RedhawkFileRenameExclusiveReadLockStrategy();
                Long timeout = (Long) params.get("readLockTimeout");
                if (timeout != null) {
                    readLockStrategy.setTimeout(timeout);
                }
                Long checkInterval = (Long) params.get("readLockCheckInterval");
                if (checkInterval != null) {
                    readLockStrategy.setCheckInterval(checkInterval);
                }
                return readLockStrategy;
            } else if ("changed".equals(readLock)) {
                GenericFileExclusiveReadLockStrategy<RedhawkFileContainer> readLockStrategy = new RedhawkFileChangedExclusiveReadLockStrategy();
                Long timeout = (Long) params.get("readLockTimeout");
                if (timeout != null) {
                    readLockStrategy.setTimeout(timeout);
                }
                Long checkInterval = (Long) params.get("readLockCheckInterval");
                if (checkInterval != null) {
                    readLockStrategy.setCheckInterval(checkInterval);
                }
                return readLockStrategy;
            } else if ("markerFile".equals(readLock)) {
                return new RedhawkMarkerFileExclusiveReadLockStrategy();
            }
        }

        return null;
    }
}
